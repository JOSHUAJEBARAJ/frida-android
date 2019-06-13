import frida, sys
 
ss = """

Java.perform(function() {

        var   main = Java.use("sg.vantagepoint.test.MainActivity");

       main.getvalue.implementation = function() {
            console.log("In function A");
             return true;
         }  
   

});

"""
device = frida.get_usb_device()
pid = device.spawn(["sg.vantagepoint.test"])
session = device.attach(pid)
script = session.create_script(ss)
script.load()
device.resume(pid)
raw_input()
