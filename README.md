[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lhanke-dev_modelpool&metric=alert_status)](https://sonarcloud.io/dashboard?id=lhanke-dev_modelpool)
[ ![Download](https://api.bintray.com/packages/lhanke-dev/mvn/modelpool/images/download.svg) ](https://bintray.com/lhanke-dev/mvn/modelpool/_latestVersion)

# ModelPool
Generate Java model instances e.g. for testing in a lean, easy and reusable way.

**Hint**: _The project is in an experimental state. Breaking changes may occur. They will be documented as part of the corresponding release. Feedback is highly appreciated._

## Getting started

First you need to declare your dependency to the ModelPool project.

### Maven Dependency

Add the following snippet to your maven repositories configuration in pom.xml:
```
<repository>
    <id>Bintray/ModelPool</id>
    <name>ModelPool</name>
    <url>https://dl.bintray.com/lhanke-dev/mvn</url>
</repository>
```

You can then declare the dependency in your dependencies section in the following way:
```
<dependency>
    <groupId>de.lhankedev.modelpool</groupId>
    <artifactId>core</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle Dependency

First you need to add to the repositories configuration like this:

Groovy:
```
maven {
    name 'Bintray/ModelPool'
    url 'https://dl.bintray.com/lhanke-dev/mvn'
}
```

Kotlin:
```
maven {
    name = "Bintray/ModelPool"
    url = uri("https://dl.bintray.com/lhanke-dev/mvn")
}
```

In your dependencies section declare the dependency to ModelPool:

Groovy:
```
testImplementation 'de.lhankedev.modelpool:core:0.0.1'
```

Kotlin:
```
testImplementation("de.lhankedev.modelpool:core:0.0.1")
```

### Example usage

ModelPool allows you to define an object model setup via a DSL that condenses text to set up a domain model.

To define and use a ModelPool object model one needs a specific domain model to create instances from and a ModelPool-definition-file where the instance graph is specified via the ModelPool DSL.
The following example shall give you an idea of how to use ModelPool with a simple domain model.

### Example Domain Model

Let's assume the following classes to build our domain model with getters and setters present:

```
package de.lhankedev.modelpool.example;

class Car {

    String manufacturer;
    Engine engine;
    Person owner;
    
    List<Integer> inspectionYears;
    List<Person> previousOwners;

}

class Engine {

    int horsePower;

}

class Person {

    String foreName;
    String lastName;

}
```

### Example ModelPool-definition

We now want to set up a reusable model. To define the instances we create a ModelPool-definition-file (`*.mp`) called e.g. `CarWithOnePrevOwner.mp`:

```
Modelname: CarWithOnePrevOwner
Namespace: package de.lhankedev.modelpool.example

Car(exampleCar):
    - manufacturer: ExampleManufacturer
    - owner: #exampleOwner
    - inspectionYears:
        - 2016
        - 2020
Person(exampleOwner):
    - foreName: T
    - lastName: Rex
exampleCar>Engine:
    - horsePower: 160
exampleCar.previousOwners>Person(examplePreviousOwner):
    - foreName: Dino
    - lastName: Saur
Person(newOwner):
    - foreName: Ele
    - lastName: Phant
```

Let's go through the above definitions:

#### Modelname - Mandatory
The unique name of the model definition that can be used to fetch the `ModelPool` instance from the `ModelPoolFactory` via `ModelPoolFactory#createModel(String)`.

#### Namespace - Optional
The namespace to resolve the model classes against. If one uses classes from only one model package the Namespace can be used to omit fully qualified classnames and just use simple classnames instead.
In case the Namespace is left out, all classes need to be defined using their fully qualified names.
A mixture of fqn and a namespace definition is (not yet) possible.

#### Class Instance Definition - At least one is mandatory
Instances of classes can be defined in the following way:

###### Object definition

> Object Class Specification - Mandatory
> 
> ```
> exampleCar.previousOwners>Person(examplePreviousOwner)
> ```
> 
> **Person**: Defines an instance of a class via simple or fq classname

> Parent Specification - Optional
> 
> ```
> exampleCar.previousOwners>Person(examplePreviousOwner)
> ```
>
> **exampleCar.previousOwners>**: Defines an attribute of another object that is part of this ModelPool definition, that should hold a reference to this object.
> The attribute name can be left out in case the parent object has only one reference to the class type specified in this object. (see e.g. the relationship Car - Engine)

> Parent Specification - Optional
>
> ```
> exampleCar.previousOwners>Person(examplePreviousOwner)
> ```
> 
> **(examplePreviousOwner)**: Defines a unique identifier (unique inside the *.mp file) that can be used to retrieve the instance from the created `ModelPool`.

###### Object attribute specification

Attributes are listed via hyphens under an object definition and are specified in a `key:value` based manner.

> Primitive attributes
> 
> ```
> - horsePower: 160
> ```
> 
> Primitive attribute values can be specified directly. Use the name of the corresponding field as key. String values do not need to be quoted. The type conversion is triggered automatically based on the type of the field. If the field has a setter available it will be set via setter. Otherwise ModelPool tries to inject the field value directly. 

> Object references
>
> ```
> - owner: #exampleOwner
> ```
> 
> Object references can be injected by using the unique id of the target object prefixed with an `#` symbol. This is an alternative to using the parent object notation.

> List values
>
> ```
> - inspectionYears:
>   - 2016
>   - 2020
> ```
> 
> List values can be specified by using a new list with items enumerated by hyphens. As value per item a primitive value or a reference (depending on the type of the list items) can be used.

### Example usage in a unit test

Via the ModelPoolFactory a model can be created and used e.g. in a unit test like this:

```
@Test
void testSellCar() {
    final ModelPoolFactory factory = ModelPoolFactory.getInstance();
    final ModelPool pool = factory.createModel("CarWithOnePrevOwner");

    final Car car = pool.getObjectById("exampleCar", Car.class);
    final Person oldOwner = pool.getObjectById("exampleOwner", Person.class);
    final Person newOwner = pool.getObjectById("newOwner", Person.class);
        
    assertThat(car)
        .isOwnedBy(oldOwner);
    cut.sellCar(car, newOwner);
    assertThat(car)
        .isOwnedBy(newOwner);
}
```

## Reference

The following section shall give more detailed insights in how ModelPool works internally. 

### Reflective Implementation

The default implementation of ModelPool uses a reflection based approach to create and manage your model instances.
Here are some advises to make the reflective approach work with your model properly:

1. Provide default constructors: In order for ModelPool to be able to instantiate your classes properly they need to provide a no-args constructor
2. Provide setter for private fields used with ModelPool: To avoid problems with the Security Manager you should define setter-methods for your private fields that you specify values for via ModelPool. It will automatically detect the setter methods by name (take care of proper naming) and use them instead of trying to access the private field directly.

#### Classpath resource scanning

To make ModelPool pick up your definition files your just need to have them on your runtime classpath. (e.g. by putting them in the `src/main/resources` folder)
ModelPool will identify the files by their file suffix `.mp` and add them to the ModelFactory definition cache on first access of the factory.

## Development

After cloning the repository you can perform the following maven tasks to test/build the project locally:

Checkstyle:
```
./gradlew check -x test
```

Tests:
```
./gradlew test
```
During test execution coverage is taken by jacoco automatically and can be found under `build/reports/jacoco/test/html/index.html`

Install to local Maven repository:
```
./gradlew publishBintrayPublicationToMavenLocal
```