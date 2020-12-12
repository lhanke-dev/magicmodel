Modelname: CarTestToNParentReference;
Namespace: de.lhankedev.magicmodel.model;

Car(testCar):
    - manufacturer: TestManufacturer
    - model: TestModel
testCar.previousOwners>Person:
    - foreName: Ele
    - lastName: Phant
    - age: 28
testCar.previousOwners>Person:
    - foreName: Dino
    - lastName: Saur
    - age: 21