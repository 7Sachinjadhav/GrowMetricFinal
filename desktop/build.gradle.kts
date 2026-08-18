plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

sourceSets {
    main {
        kotlin {
            srcDir("src/jvmMain/kotlin")
            srcDir("../app/src/main/java")
            exclude("**/MainActivity.kt")
            exclude("**/ReportExporter.kt")
        }
    }
}



dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
}

compose.desktop {
    application {
        mainClass = "com.wealthmetric.desktop.MainKt"
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
            )
            packageName = "WealthMetric"
            packageVersion = "1.0.0"
            description = "WealthMetric - Simulate. Plan. Prosper."
            copyright = "© 2026 WealthMetric"
            vendor = "WealthMetric"
        }
    }
}
