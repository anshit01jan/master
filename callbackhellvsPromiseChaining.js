// Start the coffee machine - 2 seconds
// Grind the coffee beans - 1 seconds
// Boil the water - 1.5 seconds
// Pour boiling water into the cup - 0.5 seconds
// Add ground coffee to the cup - 0.5 seconds
// Stir the coffee - 1 seconds
// Enjoy your coffee - 0.5 seconds

function startCoffeeMachine(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Coffee machine started");
            resolve("Coffee machine ready");
        }, 2000);
    });
}

function grindCoffeeBeans(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Coffee beans ground");
            resolve("Ground coffee beans");
        }, 1000);
    });
}

function boilWater(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Water boiled");
            resolve("Boiled water");
        }, 1500);
    });
}

function pourWater(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Water poured into cup");
            resolve("Poured water");
        }, 500);
    });
}

function addCoffee(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Coffee added to cup");
            resolve("Added coffee");
        }, 500);
    });
}

function stirCoffee(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Coffee stirred");
            resolve("Stirred coffee");
        }, 1000);
    });
}

function enjoyCoffee(){
    return new Promise((resolve) => {
        setTimeout(() => {
            console.log("Enjoying coffee");
            resolve("Enjoyed coffee");
        }, 500);
    });
}

function makeCoffee(){
    startCoffeeMachine()
        .then(result1 => {
            console.log(result1);
            return grindCoffeeBeans();
        })
        .then(result2 => {
            console.log(result2);
            return boilWater();
        })
        .then(result3 => {
            console.log(result3);
            return pourWater();
        })
        .then(result4 => {
            console.log(result4);
            return addCoffee();
        })
        .then(result5 => {
            console.log(result5);
            return stirCoffee();
        })
        .then(result6 => {
            console.log(result6);
            return enjoyCoffee();
        })
        .then(result7 => {
            console.log(result7);
        })
        .catch(error => {
            console.error("An error occurred: " + error);
        });
}

makeCoffee();