package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.interceptors;

/*
 * Copyright (C) 2015 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.ByteString;

import static com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.OAuthContractKt.*;
import static com.cleveroad.bootstrap.kotlin_auth.utils.ConstantsKt.EMPTY_STRING;

public final class OAuth1SigningInterceptor implements Interceptor {

    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_VERSION_VALUE = "1.0";

    private final Random random = new SecureRandom();
    private final Clock clock = new Clock();

    private String consumerKey;
    private String consumerSecret;
    private String callback;
    private String requestToken;
    private String requestTokenSecret;
    private String verifier;

    @Override public Response intercept(Chain chain) throws IOException {
        return chain.proceed(signRequest(chain.request()));
    }

    public Request signRequest(Request request) throws IOException {
        byte[] nonce = new byte[32];
        random.nextBytes(nonce);
        String oauthNonce = ByteString.of(nonce).base64().replaceAll("\\W", "");
        String oauthTimestamp = clock.millis();

        if (ConfigProvider.INSTANCE.getConfig() != null) {
            consumerKey = ConfigProvider.INSTANCE.getConfig().getConsumerKey();
            consumerSecret = ConfigProvider.INSTANCE.getConfig().getConsumerSecret();
            callback = ConfigProvider.INSTANCE.getConfig().getCallbackUrl();
            requestToken = ConfigProvider.INSTANCE.getConfig().getRequestToken();
            requestTokenSecret = ConfigProvider.INSTANCE.getConfig().getRequestTokenSecret();
            verifier = ConfigProvider.INSTANCE.getConfig().getVerifier();
        }

        checkOAuthParams();

        String consumerKeyValue = UrlEscapeUtils.escape(consumerKey);
        String callbackValue = UrlEscapeUtils.escape(callback);
        String requestTokenValue = UrlEscapeUtils.escape(requestToken);
        String verifierValue = UrlEscapeUtils.escape(verifier);

        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put(PARAM_CALLBACK, callbackValue);
        parameters.put(PARAM_CONSUMER_KEY, consumerKeyValue);
        parameters.put(PARAM_NONCE, oauthNonce);
        parameters.put(PARAM_TIMESTAMP, oauthTimestamp);
        parameters.put(PARAM_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
        parameters.put(PARAM_VERSION, OAUTH_VERSION_VALUE);

        if (!requestTokenValue.equals(EMPTY_STRING)) {
            parameters.put(PARAM_TOKEN, requestTokenValue);
        }
        if (!verifierValue.equals(EMPTY_STRING)) {
            parameters.put(PARAM_VERIFIER, verifierValue);
        }

        HttpUrl url = request.url();
        for (int i = 0; i < url.querySize(); i++) {
            parameters.put(UrlEscapeUtils.escape(url.queryParameterName(i)),
                    UrlEscapeUtils.escape(url.queryParameterValue(i)));
        }

        Buffer body = new Buffer();

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            requestBody.writeTo(body);
        }

        while (!body.exhausted()) {
            long keyEnd = body.indexOf((byte) '=');
            if (keyEnd == -1) throw new IllegalStateException("Key with no value: " + body.readUtf8());
            String key = body.readUtf8(keyEnd);
            body.skip(1); // Equals.

            long valueEnd = body.indexOf((byte) '&');
            String value = valueEnd == -1 ? body.readUtf8() : body.readUtf8(valueEnd);
            if (valueEnd != -1) body.skip(1); // Ampersand.

            parameters.put(key, value);
        }

        Buffer base = new Buffer();
        String method = request.method();
        base.writeUtf8(method);
        base.writeByte('&');
        base.writeUtf8(
                UrlEscapeUtils.escape(request.url().newBuilder().query(null).build().toString()));
        base.writeByte('&');

        boolean first = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!first) base.writeUtf8(UrlEscapeUtils.escape("&"));
            first = false;
            base.writeUtf8(UrlEscapeUtils.escape(entry.getKey()));
            base.writeUtf8(UrlEscapeUtils.escape("="));
            base.writeUtf8(UrlEscapeUtils.escape(entry.getValue()));
        }

        String signingKey =
                UrlEscapeUtils.escape(consumerSecret) + "&" + UrlEscapeUtils.escape(requestTokenSecret);

        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
        byte[] result = mac.doFinal(base.readByteArray());
        String signature = ByteString.of(result).base64();

        StringBuilder authBuild = new StringBuilder();

        authBuild.append("OAuth " + PARAM_CALLBACK + "=\"")
                .append(callbackValue).append("\", ")
                .append(PARAM_CONSUMER_KEY)
                .append("=\"")
                .append(consumerKeyValue)
                .append("\", ")
                .append(PARAM_NONCE)
                .append("=\"")
                .append(oauthNonce)
                .append("\", ");

        if (!requestTokenValue.equals(EMPTY_STRING)) {
            authBuild.append(PARAM_TOKEN + "=\"").append(requestTokenValue).append("\", ");
        }
        if (!verifierValue.equals(EMPTY_STRING)) {
            authBuild.append(PARAM_VERIFIER + "=\"").append(verifierValue).append("\", ");
        }

        authBuild.append(PARAM_SIGNATURE + "=\"")
                .append(UrlEscapeUtils.escape(signature))
                .append("\", ")
                .append(PARAM_SIGNATURE_METHOD)
                .append("=\"")
                .append(OAUTH_SIGNATURE_METHOD_VALUE)
                .append("\", ")
                .append(PARAM_TIMESTAMP)
                .append("=\"")
                .append(oauthTimestamp)
                .append("\", ")
                .append(PARAM_VERSION)
                .append("=\"")
                .append(OAUTH_VERSION_VALUE)
                .append("\"");

        return request.newBuilder().addHeader("Authorization", authBuild.toString()).build();
    }

    private void checkOAuthParams() {
        consumerKey = (consumerKey == null ? EMPTY_STRING : consumerKey);
        consumerSecret = (consumerSecret == null ? EMPTY_STRING : consumerSecret);
        callback = (callback == null ? EMPTY_STRING : callback);
        requestToken = (requestToken == null ? EMPTY_STRING : requestToken);
        requestTokenSecret = (requestTokenSecret == null ? EMPTY_STRING : requestTokenSecret);
        verifier = (verifier == null ? EMPTY_STRING : verifier);
    }

    /** Simple clock like class, to allow time mocking. */
    static class Clock {
        /** Returns the current time in milliseconds divided by 1K. */
        public String millis() {
            return Long.toString(System.currentTimeMillis() / 1000L);
        }
    }
}