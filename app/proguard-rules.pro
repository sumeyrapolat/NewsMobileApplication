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

# Prevent Gson model classes from being obfuscated
-keep class com.example.newsmobileapplication.model.entities.** { *; }

# Keep attributes used by Gson such as @SerializedName
-keepattributes *Annotation*

# Prevent Gson classes from being obfuscated
-keep class com.google.gson.** { *; }

# Preserve fields and members annotated with @SerializedName in model classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName *;
}

# Prevent issues related to generics by keeping signatures
-keepattributes Signature

# Gson için gerekli kurallar
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class * extends com.google.gson.TypeAdapter

