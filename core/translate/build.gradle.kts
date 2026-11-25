import mihon.gradle.Config

plugins {
    alias(mihonx.plugins.android.library)
    alias(mihonx.plugins.spotless)
}

android {
    namespace = "mihon.translate"

    sourceSets {
        getByName("main") {
            if (Config.includeTranslate) {
                kotlin.directories.add("src/mlkit/kotlin")
            } else {
                kotlin.directories.add("src/noop/kotlin")
            }
        }
    }
}

dependencies {
    if (Config.includeTranslate) {
        implementation(libs.mlkit.text.latin)
        implementation(libs.mlkit.text.japanese)
        implementation(libs.mlkit.text.chinese)
        implementation(libs.mlkit.text.korean)
        implementation(libs.mlkit.translate)
    }
}
