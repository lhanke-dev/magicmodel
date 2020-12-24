Modelname: CarTestToNChildReference
Namespace: de.lhankedev.magicmodel.model

Car(testCar):
    - manufacturer: TestManufacturer
    - model: TestModel
    - previousOwners:
        - #elePhant
        - #dinoSaur
Person(elePhant):
    - foreName: Ele
    - lastName: Phant
    - age: 28
Person(dinoSaur):
    - foreName: Dino
    - lastName: Saur
    - age: 21