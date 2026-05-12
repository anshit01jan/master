function fetchUserData(user, callback){
    // Simulate an API call with a timeout
    setTimeout(() => {
        const userData = {
            1 : { name: 'John Doe', age: 30 },
            2 : { name: 'Jane Smith', age: 25 }
        };
        const userObj = userData[user];
        if(userObj){
            callback(userObj, null);
        }
        else{
            callback(null, 'User not found');
        }
    }, 5000);
}


function handleUserData(user, error){
    if(error){
        console.log('Error fetching user data');
    }
    else{
        console.log("User" , user);
    }
}

fetchUserData(1, handleUserData);
fetchUserData(3, handleUserData);