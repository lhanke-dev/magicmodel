Modelname: CarTestChildReference;
Namespace: de.lhankedev.magicmodel.model;

Car(testCar):
    - manufacturer: TestManufacturer
    - model: TestModel
    - engine: #testEngine
    - owner: #testOwner
Engine(testEngine):
    - horsePower: 205
    - displacement: 4009
Person(testOwner):
    - foreName: Dino
    - lastName: Saur
    - age: 21