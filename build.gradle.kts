plugins {
    id("org.sonarqube") version sonarQubeVersion
}

sonarqube {
    properties {
        property("sonar.projectKey", "lhanke-dev_modelpool")
        property("sonar.organization", "lhanke-dev")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks {
    getByName("sonarqube").dependsOn("test")
}