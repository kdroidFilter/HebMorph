plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.code972.hebmorph"
version = libs.versions.lucene.get()
description = "Enabling Hebrew search in Lucene. Part of the HebMorph project, for making Hebrew properly searchable"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.lucene.core)
    api(libs.lucene.analysis.common)
    api(libs.lucene.queryparser)

    testImplementation(libs.lucene.memory)
    testImplementation(libs.lucene.test.framework)
    testImplementation(libs.commons.lang3)
    testImplementation(libs.junit4)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "hebmorph-lucene"
            version = project.version.toString()

            // ensure sources/javadoc jars are attached
            artifact(tasks.named("sourcesJar"))
            artifact(tasks.named("javadocJar"))

            pom {
                name.set("hebmorph-lucene")
                description.set(project.description)
                url.set("http://code972.com/hebmorph")
                licenses {
                    license {
                        name.set("Affero GPL3")
                        url.set("http://www.gnu.org/licenses/agpl.html")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("synhershko")
                        name.set("Itamar Syn-Hershko")
                        email.set("itamar@code972.com")
                    }
                }
                scm {
                    url.set("https://github.com/synhershko/HebMorph")
                    connection.set("scm:git:ssh://git@github.com/synhershko/HebMorph.git")
                    developerConnection.set("scm:git:ssh://git@github.com/synhershko/HebMorph.git")
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    val prop = (project.findProperty("hebmorph.hspell.path") as String?) ?: System.getenv("HEBMORPH_HSPELL_PATH")
    if (prop != null && prop.isNotBlank()) {
        systemProperty("hebmorph.hspell.path", prop)
    } else {
        val fallback = project.projectDir.toPath().resolve("../hspell-data-files").normalize().toFile()
        if (fallback.exists()) {
            systemProperty("hebmorph.hspell.path", fallback.absolutePath)
        }
    }
}
