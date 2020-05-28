package com.cleveroad.progress_upload_example.network.modules

import com.cleveroad.progress_upload_example.models.converters.Converter

abstract class BaseRxModule<T, NetworkModel, M>(val api: T, val converter: Converter<NetworkModel, M>)