# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Periodicity: It uses class name to identify and deserialize the object w/ GSON to store in Room
-keep class com.eyther.lumbridge.shared.time.model.Periodicity { *; }
-keepclassmembers class com.eyther.lumbridge.shared.time.model.Periodicity$EveryXDays { *; }
-keepclassmembers class com.eyther.lumbridge.shared.time.model.Periodicity$EveryXWeeks { *; }
-keepclassmembers class com.eyther.lumbridge.shared.time.model.Periodicity$EveryXMonths { *; }
-keepclassmembers class com.eyther.lumbridge.shared.time.model.Periodicity$EveryXYears { *; }

# Retrofit classes. We normally used @SerializedName to avoid the need of this rule,
# but better safe than sorry, so add all Retrofit Remote classes here.
-keep class com.eyther.lumbridge.data.model.currencyexchange.remote.** { *; }
