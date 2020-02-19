-keep public class com.crazylegend.subhub.adapters.localVideos.LocalVideoViewHolder { <methods>; }
-keep public class com.crazylegend.subhub.adapters.chooseLanguage.LanguageViewHolder { <methods>; }
-keep public class com.crazylegend.subhub.adapters.subtitles.SubtitlesViewHolder { <methods>; }
-keep public class com.crazylegend.subhub.adapters.folderSources.PickedDirViewHolder { <methods>; }
-keep public class com.crazylegend.subhub.pickedDirs.PickedDirModel { <methods>; }
-keep public class com.masterwok.opensubs.models.OpenSubtitleItem { *; }


-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.***