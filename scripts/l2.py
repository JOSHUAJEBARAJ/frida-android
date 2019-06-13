import frida, sys
 
ss = """
Java.perform(function() {

        var   main = Java.use("java.lang.System");

       main.exit.overload("int").implementation = function() {
            console.log("In function A");
             
         }  
   

});
"""
device = frida.get_usb_device()
pid = device.spawn(["sg.vantagepoint.debug"])
session = device.attach(pid)
script = session.create_script(ss)
script.load()
device.resume(pid)
raw_input()
