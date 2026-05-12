const newPromise = new Promise((resolve,reject)=>{
    setTimeout(()=>{
        const num = Math.floor(Math.random() * 10);
        if(num > 5){
            resolve(num);
        }
        else{
            reject(new Error("Number is less than 5"));
        }
    }, 3000);
})

newPromise
    .then(result => {
        console.log("Promise resolved with number: " + result);
    })
    .catch(error => {
        console.log("Promise rejected with error: " + error.message);
    });

