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
- a date defined relatively to Easter Sunday with a positive or negative offset of days
- a fixed date with a condition to make the holiday effective
- a fixed date with an alternative date as replacement under the given condition

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

## Usage

### Gradle

```groovy
repositories {
    mavenCentral()
}
dependencies {
    compile 'org.itsallcode:holiday-calculator:0.2.0'
}
```

### Maven

```xml
<dependency>
    <groupId>org.itsallcode</groupId>
    <artifactId>holiday-calculator</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Calculating holidays

In order to calculate holidays you must create a holiday definition.
Holiday-calculator supports 4 different flavors of holiday definitions.
For each flavor there is a dedicated class in package `org.itsallcode.holidays.calculator.logic`.

1. `FixedDateHoliday` defines a holiday with a fixed date.
2. `FloatingHoliday` defines a holiday with a floating date.
3. `EasterBasedHoliday` defines an Easter-based holiday.
4. `OrthodoxEasterBasedHoliday` defines an Orthodox-Easter-based holiday
5. `ConditionalHoliday` defines a conditional holiday, effective under the given condition
5. `HolidayWithAlternative` defines a holiday with an alternative date, effective under the given condition

Section [Configuration file](README.md#flavors) describes the details and parameters for each flavor.

#### Instantiating subclasses of Holiday

The following code sample instantiates one holiday for each of these flavors:

```
import org.itsallcode.holidays.calculator.logic.variants.FixedDateHoliday;
import org.itsallcode.holidays.calculator.logic.variants.FloatingHoliday;
import org.itsallcode.holidays.calculator.logic.variants.EasterBasedHoliday;
import org.itsallcode.holidays.calculator.logic.variants.OrthodoxEasterBasedHoliday;
import org.itsallcode.holidays.calculator.logic.variants.ConditionalHoliday;
import org.itsallcode.holidays.calculator.logic.variants.HolidayWithAlternative;
import org.itsallcode.holidays.calculator.logic.conditions.DayOfWeekCondition;

class MyClass {

    public MyClass() {
        Holiday h1 = new FixedDateHoliday("holiday", "Christmas Eve", MonthDay.of(12, 24));

        Holiday h2 = new FloatingHoliday(
            "holiday", "Father's Day", 3, DayOfWeek.SUNDAY, Direction.AFTER, MonthDay.of(6, 1));

        Holiday h3 = new EasterBasedHoliday("holiday", "Good Friday", -2);

        Holiday h4 = new OrthodoxEasterBasedHoliday(
            "holiday", "Orthodox Good Friday", -2);

        ConditionBuilder dec25SatSun = new ConditionBuilder()
            .withDaysOfWeek(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
            .withPivotDate(MonthDay.of(12, 25));
        Holiday h5_default = new FixedDateHoliday("holiday", "Bank Holiday 1", MonthDay.of(12, 27));
        Holiday h5 = new ConditionalHoliday(dec25SatSun, h5_default);
        
        Holiday h6_default = new FixedDateHoliday("holiday", "Koningsdag", MonthDay.of(4, 27));
            ConditionBuilder isSunday = new ConditionBuilder().withDaysOfWeek(DayOfWeek.SUNDAY);
            Holiday h6 = new HolidayWithAlternative(defaultKoningsdag, isSunday, MonthDay.of(4, 26));

        Holiday h7 = new FloatingHoliday("holiday", "Midsommarafton",
            1, DayOfWeek.SATURDAY, Direction.BEFORE, MonthDay.of(6, 26))
            .withOffsetInDays(-1);
    }
}
```

Holiday `h5` is a conditional holiday with a negated condition, see [below](README.md#conditional-holidays).
Holiday `h6` is a holiday with an alternative date, see [below](README.md#alternative-holidays).
Holiday `h7` is a floating holiday with an additional offset in days, see [below](README.md#floating-holidays).

#### Parsing a configuration File

Besides creating instances of the subclasses of `Holiday` you can also use a configuration file to define your personal selection of holidays. Class `HolidaysFileParser` then parses the file and returns a list of holidays:

```
import org.itsallcode.holidays.calculator.logic.parser.HolidaysFileParser

class MyClass {
    public MyClass() {
        List<Holiday> holidays = new HolidaysFileParser.parse("/path/to/holidays.cfg")
    }
}
```

For the seven example holidays instantiated above, the content of the file could look like

```
# my holidays

holiday fixed  12 24 Christmas Eve
holiday float      3 SUN after 6 1 Father's Day
holiday easter    -2 Good Friday
holiday orthodox-easter -2 Orthodox Good Friday
holiday if    DEC 25 is Sat,Sun then fixed DEC 27 Bank Holiday
holiday either fixed 4 27 or if SUN then fixed 4 26 Koningsdag
holiday float     1 day before 1 Sat before JUN 26 Midsommarafton
```

Section [Configuration file](README.md#flavors) describes the syntax in detail.

#### Evaluating a specific holiday for a specific year

In order to evaluate a holiday for the current or any other year and hence get an instance of this
holiday with a concrete date, you can just call method `Holiday.of()`, supplying the year as argument:

```
Holiday goodFriday = new EasterBasedHoliday("holiday", "Good Friday", -2);
LocalDate gf_2021 = goodFriday.of(2021); // 2021 April 4th
```

### Configuration file

User can set up his or her individual personal list of favorite holidays using the supported formula flavors.
Holiday-calculator already provides configuration files for a growing list of countries in folder [holidays](holidays).

#### <a name="flavors"></a>Content of configuration file

The configuration file is organized in lines. Each line can contain one of 7 types of content:

1. Empty
2. Definition of a holiday with a fixed date
3. Definition of a holiday with a floating date
4. Definition of an Easter-based holiday 
5. Definition of an Orthodox-Easter-based holiday
6. Definition of a conditional holiday
7. Definition of a holiday with an alternative date

All other lines are rated as illegal and ignored by holiday-calculator, logging an error message.

Whitespace is allowed in most places without changing the nature of the
line. Hence, a line containing nothing but tabs and spaces is still rated to
be an empty line.

##### Comments

Each line can contain an optional comment starting with hash mark `#`.
Holiday-calculator will ignore the rest of the line after and including the
hash mark.

##### Holiday definitions

The definition of any holiday starts with a *category*.  The category is an arbitrary
string of non-whitespace characters. The application evaluating your holidays
might support different categories of holidays, e.g. birthdays, anniversaries,
etc. and may display them in different colors. As a default we propose to use
category "holiday".

The category is followed by a *tag* identifying the flavor of the holiday
definition and additional arguments depending on the flavor. The last argument
is always a string containing the name of the holiday.

General rules
- All strings except the name of the holiday are case-insensitive.
- In all definitions including a month the month must be specified by
    - either using its English name January, February, March, April, May, June, July, August, September, October, November, December
    - or a unique abbreviation of the name 
    - or the number of a month
  with 1 for January and 12 for December.
- Day of month is an integer from 1 to 31.
- Day of week is 
    - either one of the English names Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    - or a unique abbreviation of it

In the following cases holiday-calculator will log an error message and ignore
the holiday definition:
- if the tag does not match any of the three supported tags
  "fixed", "float", "easter"
- if the holiday definition contains illegal numbers, such as month 0 or 13,
  day 32, or day 30 for February
- if the day of week does not match the abbreviation of any of the English names 
- if the day of week is abbreviated ambiguously, e.g. "T" or "S"
- if the name of a month does not match the abbreviation of any of the English names 
- if the name of a month is abbreviated ambiguously, e.g. "Ma" or "Ju"

##### Fixed date holiday definition

A fixed date holiday definition has the tag "fixed", followed by the month and day of month.

Syntax: `holiday fixed <month> <day> <name>`

Samples:
- `holiday fixed   1  1 New Year`
- `holiday fixed Dec 12 Christmas Eve`

##### <a name="floating-holidays"></a>Floating holiday definition

A floating holiday definition has the tag "float", followed by the offset, the
day of week, the direction "before" or "after", the month and day
of month.  Month and day of month specify a *pivot date*.

If the day of week of the pivot date is identical to the specified one then
the offset starts to count on the pivot date, otherwise on the next instance
of the specified weekday before or after the pivot date.

Instead of a number the day of month can also be the special string "last-day".

Syntax: `holiday float <offset> <direction> <day of week> <month> <day> <name>`

Samples:
- `holiday float 1 W after 1 1 First Wednesday on or after New Year`
- `holiday float 2 MON before DEC last-day Second Monday before New Year's eve, December the 31st`
- `holiday float 4 SUNDAY before 12 24 First Advent`

##### Floating holiday with additional offset in days

Optionally a floating holiday definition can contain an additional offset in days. 
This is especially required for Swedish holiday "Midsommarafton", which is 1 day before midsummer day, 
which in turn is the first Saturday on or before June, 26th.

Syntax: `holiday float <offset2> day[s] <direction2> <offset> <direction> <day of week> <month> <day> <name>`

Samples:
- `holiday float 1 day before 1 Sat before JUN 26 Midsommarafton`

##### Easter-based holiday definition

An Easter-based holiday definition has the tag "easter", followed by the
offset. The offset is the number of days from Easter Sunday. If offset is
negative then the holiday is before Easter Sunday, otherwise after.

Syntax: `holiday easter <offset> <name>`

Samples:
- `holiday easter   0 Easter Sunday`
- `holiday easter  -2 Good Friday`
- `holiday easter +49 Pentecost Sunday`

##### Orthodox-Easter-based holiday definition

An Orthodox-Easter-based holiday definition has the tag "orthodox-easter", followed by the
offset. The offset is the number of days from Easter Sunday. If offset is
negative then the holiday is before Orthodox-Easter Sunday, otherwise after.

Syntax: `holiday orthodox-easter <offset> <name>`

Samples:
- `holiday orthodox-easter   0 Orthodox Easter Sunday`
- `holiday orthodox-easter  -2 Orthodox Good Friday`
- `holiday orthodox-easter +49 Orthodox Pentecost Monday`


##### <a name="conditional-holidays"></a>Conditional holidays

Some countries have a few holidays that do not occur every year but only if a specific condition is met.
Examples are the bank holidays in the UK.
Holiday-calculator calls such holidays *conditional holidays*.

A conditional holiday definition has the tag "if", followed by
- pivot month 
- pivot day of month
- "is"
- "not" (optional)
- comma-separated list of pivot days of week, e.g. "Wed,Fri", not containing any whitespace.
- "then"
- "fixed"
- month 
- day of month
- name

The conditional holiday is only effective, if the pivot-date of the conditions falls on one of the specified days of week.
If the definition contains the optional "not", then the pivot day must *not fall* on one of the specified days of week.

Syntax: `holiday if <pivot month> <pivot day> is [not] <days of week> then fixed <month> <day> <name>`

Samples:
- `holiday if DEC 25 is Sat,Sun then fixed DEC 27 Bank Holiday`
- `holiday if DEC 25 is not Fri,Sat then fixed DEC 26 Boxing day is extra day off`

##### <a name="alternative-holidays"></a>Alternative date Holidays

Some countries have holidays that are moved to another date in case a specific condition is met.
An example is the "Koningsdag" in the Netherlands which occurs on April, 27th but is moved to April, 26 in case April, 27th is a Sunday.

A holiday with an alternative has the tag "either", followed by
- month
- day
- "or"
- "if"
- "not" (optional)
- comma-separated list of pivot days of week, e.g. "Wed,Fri", not containing any whitespace.
- "then"
- "fixed"
- alternative month
- alternative day of month
- name

Syntax: `holiday either <month> <day> of if [not] <days of week> then fixed <alternative month> <alternative day> <name>`

Samples:
- `holiday either 4 27 or if SUN then fixed 4 26 Koningsdag`
- `holiday either 4 27 or if not Mon,Tue,We,Thu,Fri,Sat then fixed 4 26 Koningsdag`


## Development

### Generate / update license header

```sh
./gradlew licenseFormat
```

### Check if dependencies are up-to-date

```sh
./gradlew dependencyUpdates
```

### Building

Install to local maven repository:

```sh
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

    ```sh
    ./gradlew clean build publish closeAndReleaseRepository --info
    ```

4. Create a new [release](https://github.com/itsallcode/holiday-calculator/releases) on GitHub.
5. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/org/itsallcode/holiday-calculator/).
