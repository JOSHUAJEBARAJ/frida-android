
Java.perform(function() {

        var   main = Java.use("sg.vantagepoint.root.MainActivity");

       main.isDeviceRooted.implementation = function() {
            console.log("In function A");
             return false;
         }  
   

});

