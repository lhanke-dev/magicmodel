version = "0.0.1"

plugins {
    id("common-configuration")

    antlr
    `maven-publish`
}

dependencies {
    implementation("org.reflections:reflections:${reflectionsVersion}")
    antlr("org.antlr:antlr4:${antlrVersion}")
}

sourceSets {
    val antlrGen by creating {
        java {
            setSrcDirs(listOf("src-gen/main/java"))
        }
        dependencies {
            add("antlrGenImplementation", "org.antlr:antlr4:${antlrVersion}")
        }
    }
    getByName("main") {
        java {
            setSrcDirs(listOf("src/main/java")) // remove paths appended by antlr plugin
            compileClasspath += antlrGen.output
            runtimeClasspath += antlrGen.output
        }
    }
    getByName("test") {
        java {
            setSrcDirs(listOf("src/test/java")) // remove paths appended by antlr plugin
            compileClasspath += antlrGen.output
            runtimeClasspath += antlrGen.output
        }
    }
}

java {
    @Suppress("UnstableApiUsage")
    withJavadocJar()
    @Suppress("UnstableApiUsage")
    withSourcesJar()
}

tasks {
    generateGrammarSource {
        maxHeapSize = "64m"
        outputDirectory = File(project.sourceSets.getByName("antlrGen").allJava.srcDirs.stream().findFirst().get().absolutePath)
    }

    clean {
        doLast {
            project.sourceSets.getByName("antlrGen").allJava.srcDirs.forEach {
                project.delete(fileTree(it.absolutePath))
                File(it.absolutePath).listFiles()?.forEach {
                    project.delete(it)
                }
            }
        }
    }

    compileJava {
        dependsOn("compileAntlrGenJava")
    }

    jar {
        from(sourceSets.main.get().output)
        from(sourceSets.getByName("antlrGen").output)
    }

    getByName("compileAntlrGenJava").dependsOn("generateGrammarSource")
}

checkstyle {
    toolVersion = checkstyleVersion
    configFile = file("${rootProject.projectDir.absolutePath}/config/checkstyle/checkstyle.xml")
    // exclude antlr generated sources
    sourceSets = listOf(project.sourceSets.getByName("main"), project.sourceSets.getByName("test"))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/lhanke-dev/modelpool")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            pom {
                name.set("ModelPool")
                description.set("Toolset to generate Java model instances e.g. for testing in a lean, easy and reusable way.")
                url.set("https://github.com/lhanke-dev/modelpool")
                licenses {
                    license {
                        name.set("MIT License")
                    }
                }
                developers {
                    developer {
                        id.set("lhanke")
                        name.set("Lukas Hanke")
                        email.set("lukas.hanke11@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:lhanke-dev/modelpool.git")
                    url.set("https://github.com/lhanke-dev/modelpool")
                }
            }
        }
    }
}