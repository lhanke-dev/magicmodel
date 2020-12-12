Modelname: FullExampleModel;
Namespace: de.lhankedev.magicmodel.model;

Car(testCar):
    - manufacturer: TestManufacturer
    - model: TestModel
    - engine: #testEngine
Engine(testEngine):
    - horsePower: 205
    - displacement: 4009
testCar.owner>Person:
    - foreName: Dino
    - lastName: Saur
    - age: 21