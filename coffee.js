// Start the coffee machine - 2 seconds
// Grind the coffee beans - 1 seconds
// Boil the water - 1.5 seconds
// Pour boiling water into the cup - 0.5 seconds
// Add ground coffee to the cup - 0.5 seconds
// Stir the coffee - 1 seconds
// Enjoy your coffee - 0.5 seconds

function startCoffeeMachine(callback) {
    setTimeout(() => {
        console.log("Coffee machine started");
        callback("Coffee machine ready");
    }, 2000);
}

function grindCoffeeBeans(callback) {
    setTimeout(() => {
        console.log("Coffee beans ground");
        callback("Ground coffee beans");
    }, 1000);
}

function boilWater(callback) {
    setTimeout(() => {
        console.log("Water boiled");
        callback("Boiled water");
    }, 1500);
}

function pourWater(callback) {
    setTimeout(() => {
        console.log("Water poured into cup");
        callback("Poured water");
    }, 500);
}

function addCoffee(callback) {
    setTimeout(() => {
        console.log("Coffee added to cup");
        callback("Added coffee");
    }, 500);
}

function stirCoffee(callback) {
    setTimeout(() => {
        console.log("Coffee stirred");
        callback("Stirred coffee");
    }, 1000);
}

function enjoyCoffee(callback) {
    setTimeout(() => {
        console.log("Enjoying coffee");
        callback("Enjoyed coffee");
    }, 500);
}


function makeCoffee() {
    startCoffeeMachine(function(result1) {
        grindCoffeeBeans(function(result2) {
            boilWater(function(result3) {
                pourWater(function(result4) {
                    addCoffee(function(result5) {
                        stirCoffee(function(result6) {
                            enjoyCoffee(function(result7) {
                            });
                        });
                    });
                });
            });
        });
    });
}

makeCoffee();