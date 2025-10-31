HebMorph enables high‑quality Hebrew search with Apache Lucene by providing tokenization, normalization, and morphology support tailored for Hebrew. The project aims for strong recall and precision, and is released under the GNU Affero General Public License v3.

More details: http://code972.com/HebMorph

Note: The .NET codebase and legacy components were removed. The maintained codebase is the Java Lucene module published as `hebmorph-lucene`.

## Compatibility

- HebMorph releases track Apache Lucene’s versions. Artifact versions generally match the Lucene version they target.
- For Elasticsearch, use the dedicated plugin: https://github.com/synhershko/elasticsearch-analysis-hebrew

## Getting the Library

This project is not published to Maven Central yet. Use one of the following options:

- Build from source and depend on modules directly.
- Publish to your local Maven cache and depend on it from your app:
  
  1) Publish locally
  
  ./gradlew publishToMavenLocal
  
  2) Add `mavenLocal()` to your project repositories and depend on the artifact:
  
  - Gradle (Kotlin DSL)
    
    repositories { mavenLocal(); mavenCentral() }
    dependencies { implementation("com.code972.hebmorph:hebmorph-lucene:10.3.1") }
  
  - Maven
    
    <dependency>
      <groupId>com.code972.hebmorph</groupId>
      <artifactId>hebmorph-lucene</artifactId>
      <version>10.3.1</version>
    </dependency>

  The artifact version is aligned with Lucene’s version (see `gradle/libs.versions.toml`).

- Optional: configure a remote repository (e.g., GitHub Packages) in `publishing.repositories` if you want to distribute a private or public build.

## Requirements

- Java 21+
- HSpell data files available locally (the `hspell-data-files` directory).

## Configuring HSpell Data Path

At runtime or during tests, HebMorph tries to resolve the path to `hspell-data-files` dynamically. The resolution order is:

1) Java system property: `-Dhebmorph.hspell.path=/path/to/hspell-data-files`
2) Environment variable: `HEBMORPH_HSPELL_PATH=/path/to/hspell-data-files`
3) Common relative paths near the project (e.g. `../hspell-data-files`)

If none is set and the files are not found, tests will fail with a clear error. Place the `hspell-data-files` directory next to the repository root or set one of the overrides above.

## Build

This project uses Gradle with Kotlin DSL and a centralized version catalog (`gradle/libs.versions.toml`).

- Build: `./gradlew build`
- Run tests (ensure HSpell data path is configured as above): `./gradlew test`

## Using the Analyzers (Lucene)

The primary analyzers are under `org.apache.lucene.analysis.hebrew`:

- `HebrewIndexingAnalyzer` for indexing
- `HebrewQueryAnalyzer` and `HebrewQueryLightAnalyzer` for queries
- `HebrewExactAnalyzer` for exact match use‑cases

See the test sources for usage examples.

## Recent Changes

- Build migrated to Gradle Kotlin DSL; configuration streamlined.
- Centralized dependency management via `gradle/libs.versions.toml`.
- Modernized Java target (Java 21) and updated Lucene dependency.
- Dynamic resolution of `hspell-data-files` path via system property or environment variable.
- Removed legacy .NET code and outdated components.

## License

HebMorph is copyright (C) 2010–2015, Itamar Syn‑Hershko.
HebMorph relies on Hspell, copyright (C) 2000–2013, Nadav Har'El and Dan Kenigsberg (http://hspell.ivrix.org.il/).

It is released under the GNU Affero General Public License v3. See the LICENSE file. Note that not only the programs in the distribution, but also the dictionary files and the generated word lists, are licensed under the AGPL. There is no warranty of any kind for the contents of this distribution.
