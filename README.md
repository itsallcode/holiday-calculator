# holiday-calculator
Calculate holidays from flexible formulas in a configuration file.

[![Build](https://github.com/itsallcode/holiday-calculator/workflows/Build/badge.svg)](https://github.com/itsallcode/holiday-calculator/actions?query=workflow%3ABuild)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aholiday-calculator&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aholiday-calculator)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Aholiday-calculator&metric=coverage)](https://sonarcloud.io/dashboard?id=org.itsallcode%3Aholiday-calculator)
[![Maven Central](https://img.shields.io/maven-central/v/org.itsallcode/holiday-calculator?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.itsallcode%22%20a%3A%22holiday-calculator%22)

Each holiday is meant to repeat every year and is defined by a formula in
order to compute a concrete holiday instance for a given year.
Holiday-calculator supports the following formula flavors:

- a fixed date identical for every year
- a floating date defined by a pivot date, specified as month and day and an
  offset restricted to a particular day of the week, e.g. fourth Sunday before
  Christmas, i.e. December 24th
- a date defined relatively to Easter Sunday with a positive or negative
  offset of days

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

## Configuration

User can set up his or her individual personal list of favorite holidays using
the supported formula flavors.

### Configuration file

(This section needs to be moved to the WR plugin description)

Holiday-calculator expects the configuration file named `holidays.txt` to be
located in data directory next to file `projects.json`.

See WR configuration about how to configure the location of the data
directory.

### Content of configuration file

The configuration file is organized in lines. Each line can contain one of 5
types of content:

1. Empty
2. Fixed date holiday definition
3. Floating holiday definition
4. Easter-based holiday definition
5. Orthodox-Easter-based holiday definition

All other lines are rated as illegal and ignored by holiday-calculator,
logging an error message.

Whitespace is allowed in most places without changing the nature of the
line. Hence, a line containing nothing but tabs and spaces is still rated to
be an empty line.

#### Comments

Each line can contain an optional comment starting with hash mark `#`. 
Holiday-calculator will ignore the rest of the line after and including the hash mark character.

#### Holiday definitions

All holiday definitions start with a *category*.  The category is an arbitrary
string of non-whitespace characters. The application evaluating your holidays
might support different categories of holidays, e.g. birthdays, aniversaries,
etc. and may display them in different colors. As a default we propose to use
category "holiday".

The category is followed by a *tag* identifying the flavor of the holiday
definition and additional arguments depending on the flavor. The last argument
is always a string containing the name of the holiday.

General rules
- All strings except the name of the holiday are case-insensitive.
- In all definitions including the number of a month, January is 1,
  December is 12.
- Day of month is an integer from 1 to 31.

In the following cases holiday-calculator will log an error message and ignore
the holiday definition:
- if the tag does not match any of the three supported tags
  "fixed", "float", "easter"
- if the holiday definition contains illegal numbers, such as month 0 or 13,
  day 32, or day 30 for February
- if the day of week does not match the abbreviation of any of the english
  names Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
- if the day of week is abbreviated ambiguously, e.g. "T" or "S"

#### Fixed date holiday definition

A fixed date holiday definition has the tag "fixed", followed by the numbers
of month and day of month.

Syntax: `holiday fixed <month> <day> <name>`

Sample: `holiday fixed 1 1 New Year`

#### Floating holiday definition

A floating holiday definition has the tag "float", followed by the offset, the
day of week, the direction "before" or "after", the numbers of month and day
of month.  Month and day of month specify a *pivot date*.

If the day of week of the pivot date is identical to the specified one then
the offset starts to count on the pivot date, otherwise on the next instance
of the specified weekday before or after the pivot date.

Instead of a number the day of month can also be the special string "last-day".

Syntax: `holiday float <offset> <direction> <day of week> <month> <day> <name>`

Samples:
- `holiday float 1 W after 1 1 First Wednesday on or after New Year`
- `holiday float 2 MON before 12 last-day Second Monday before New Year's eve, December the 31st`
- `holiday float 4 SUNDAY before 12 24 First Advent`

#### Easter-based holiday definition

An Easter-based holiday definition has the tag "easter", followed by the
offset. The offset is the number of days from Easter Sunday. If offset is
negative then the holiday is before Easter Sunday, otherwise after.

Syntax: `holiday easter <offset> <name>`

Samples:
- `holiday easter   0 Easter Sunday`
- `holiday easter  -2 Good Friday`
- `holiday easter +49 Pentecost Sunday`

#### Orthodox-Easter-based holiday definition

An Orthodox-Easter-based holiday definition has the tag "orthodox-easter", followed by the
offset. The offset is the number of days from Easter Sunday. If offset is
negative then the holiday is before Orthodox-Easter Sunday, otherwise after.

Syntax: `holiday orthodox-easter <offset> <name>`

Samples:
- `holiday orthodox-easter   0 Orthodox Easter Sunday`
- `holiday orthodox-easter  -2 Orthodox Good Friday`
- `holiday orthodox-easter +49 Orthodox Pentecost Monday`

## Usage

### Gradle

```groovy
repositories {
    mavenCentral()
}
dependencies {
    compile 'org.itsallcode:holiday-calculator:0.0.1'
}
```

### Maven

```xml
<dependency>
    <groupId>org.itsallcode</groupId>
    <artifactId>holiday-calculator</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Development

### Generate / update license header

```bash
$ ./gradlew licenseFormat
```
### Building

Install to local maven repository:

```bash
./gradlew clean publishToMavenLocal
```

### Publish to Maven Central

1. Add the following to your `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<your maven central username>
    ossrhPassword=<your maven central passwort>

    signing.keyId=<gpg key id (last 8 chars)>
    signing.password=<gpg key password>
    signing.secretKeyRingFile=<path to secret keyring file>
    ```

2. Increment version number in `build.gradle` and `README.md`, update [CHANGELOG.md](CHANGELOG.md), commit and push.
3. Run the following command:

    ```bash
    $ ./gradlew clean build publish closeAndReleaseRepository --info
    ```

4. Create a new [release](https://github.com/itsallcode/holiday-calculator/releases) on GitHub.
5. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/org/itsallcode/holiday-calculator/).
