class Vehicle{
    constructor(totalWheels){
        this.totalWheels = totalWheels;
    }
    getTotalWheels(){
        return this.totalWheels;
    }
}

class Car extends Vehicle{
    constructor(name, type, year, totalWheels) {
        super(totalWheels);
        this.name = name;
        this.type = type;
        this.year = year;
    }

    getInfo(){
        return `Car: ${this.name} type: ${this.type} year: ${this.year} wheels: ${this.getTotalWheels()}`;
    }
}

class Bike extends Car{
    constructor(name, type, year, color, totalWheels) {
        super(name, type, year, totalWheels);
        this.color = color;
    }

    getBikeInfo(){
        return `Bike: ${this.name} type: ${this.type} year: ${this.year} color: ${this.color} wheels: ${this.getTotalWheels()}`;
    }
}

class Truck extends Car{
    constructor(name, type, year, loadingCapacity, totalWheels) {
        super(name, type, year, totalWheels);
        this.loadingCapacity = loadingCapacity;
    }

    getTruckInfo(){
        return `Truck: ${this.name} type: ${this.type} year: ${this.year} loadingCapacity: ${this.loadingCapacity} wheels: ${this.getTotalWheels()}`;
    }
}

const bike1 = new Bike("Yamaha", "Sport", 2022, "Red", 2);
console.log(bike1.getBikeInfo());
console.log(bike1.getInfo());


const truck1 = new Truck("Ford", "Pickup", 2021, "2000kg", 8);
console.log(truck1.getInfo());
console.log(truck1.getTotalWheels());

const car = new Car('BMW', 'SUV', 2020, 4);
console.log(car.getTotalWheels());