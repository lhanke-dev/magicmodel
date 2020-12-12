plugins {
    id("common-configuration")

    antlr
    `java-library`
}

dependencies {
    implementation("org.reflections:reflections:${reflectionsVersion}")
    antlr("org.antlr:antlr4:${antlrVersion}")
}

sourceSets {
    main {
        java {
            srcDirs(
                    "src/main/java",
                    "src-gen/main/java"
            )
        }
    }
}

tasks {
    generateGrammarSource {
        maxHeapSize = "64m"
        outputDirectory = File("${projectDir}/src-gen/main/java")
    }

    clean {
        doLast {
            project.delete(fileTree("src-gen/main/java"))
            File("src-gen/main/java").listFiles()?.forEach {
                project.delete(it)
            }
        }
    }
}