Modelname: FullExampleModel
Namespace: de.lhankedev.magicmodel.model

Car(testCar):
    - manufacturer: TestManufacturer
    - model: TestModel
    - engine: #testEngine
    - previousOwners:
        - #elePhant
        - #dinoSaur
Engine(testEngine):
    - horsePower: 205
    - displacement: 4009
testCar.owner>Person:
    - foreName: Lion
    - lastName: King
    - age: 99
Person(elePhant):
    - foreName: Ele
    - lastName: Phant
    - age: 28
Person(dinoSaur):
    - foreName: Dino
    - lastName: Saur
    - age: 21