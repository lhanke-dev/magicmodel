plugins {
    id("org.sonarqube") version sonarQubeVersion
}

sonarqube {
    properties {
        property("sonar.projectKey", "lhanke-dev_magicmodel")
        property("sonar.organization", "lhanke-dev")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}