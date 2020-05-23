-keep public class com.crazylegend.subhub.adapters.* { public <init>(...); }
-keep public class com.masterwok.opensubs.models.OpenSubtitleItem { *; }

-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.***