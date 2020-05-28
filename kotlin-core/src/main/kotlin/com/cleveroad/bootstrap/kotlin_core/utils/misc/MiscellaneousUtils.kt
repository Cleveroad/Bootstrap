package com.cleveroad.bootstrap.kotlin_core.utils.misc

/**
 * Helper for miscellaneous operations
 */
class MiscellaneousUtils {

    companion object {
        private const val EXTRA = "EXTRA"
        var defaultPackageName: String = ""

        /**
         * Get String with name for EXTRA parameter. Pattern packageName + class.simpleName + EXTRA + extraName
         */
        @JvmOverloads
        fun <T> getExtra(extraName: String,
                         clazz: Class<T>? = null,
                         packageName: String = defaultPackageName): String {
            return (if (defaultPackageName.isNotBlank()) defaultPackageName else packageName)
                    .let { "${it}_${clazz?.simpleName ?: ""}_${EXTRA}_$extraName" }
        }
    }
}
