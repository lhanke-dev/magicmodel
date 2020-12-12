plugins {
    java
}

repositories {
    mavenCentral()
}



dependencies {
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.slf4j:slf4j-api:${slf4jVersion}")
    implementation("commons-io:commons-io:${apacheCommonsVersion}")

    implementation("org.mapstruct:mapstruct:${mapStructVersion}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapStructVersion}")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    testImplementation("org.assertj:assertj-core:${assertjVersion}")

    testCompileOnly("org.projectlombok:lombok:${lombokVersion}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombokVersion}")
}

tasks.test {
    useJUnitPlatform()
}