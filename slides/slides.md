## Frida For android

---

## Topics 

- Introduction to  apk
- File inside the apk
- Reversing the apps
- frida
- Dynamic  Binary Instrumentatation
- Common Challenges while pentesting Android
- Modes of operation
- Frida Installation(Hands-on)
- Frida Common Api for Android 
- Hands on
- Frida Gadget
- References


---

# What is apk?

> file [appname]


![app](zip.jpg)


---
### Unzipping the apk

Open the gitbook and follow the instruction

---

### Files inside the apk
 
 - META-INF
 - lib
 - res
 - assets
 - AndroidManifest.xml
 - classes.dex
 - resources.arsc



---
# Reversing our first app

Open the gitbook and try to follow the apktool section





---
# Reversing the App (Human-readable way)

 Open the gitbook and try to read the Reversing-the app section

---

## What is Frida


- Dynamic binary instrumentation tool

![inject-tool](https://cdn-images-1.medium.com/max/1200/1*k95Y339RMzb9zPVEEz-Z9g.png)


---
## What is instumentation?


## Types of instrumentation

- Source Instrumentation
- Binary Instrumentation

---
## What is Dynamic Binary Instrumentation

## Types of Dynamic  Instrumentation

- Injected

- Embeded


---
## Why frida

How we change the app logic ?

## Smali reversing

![smali](http://mstajbakhsh.ir/wp-content/uploads/2016/12/Smali-Code.png)

---
![no-](https://i.imgur.com/RUdPyQP.jpg)
---

## Dynamic Binary Instumenation

![frida](https://cdn-images-1.medium.com/max/1050/1*5JNEHSsnrE1VLfl5mpXuFg.png)


---
## Common Challenges While pentsting android apps 
- Root Bypassing
- Anti Debugging
- Anti Emulation
- change Logic  
---
# Modes of operation

## Injected
- spawn an existing process
- hook to the running program
- Requires the root access

---

## Embeded

- Frida-gadget  a shared library


## Preloaded
- Using a dynamic linker feature like LD_PRELOAD or DYLD_INSERT_LIBRARIES
- Not used in android
---

## Frida Installation

open the gitbook Try to install Frida 
---
## Frida Basics

open the gitbook Try to run the basic Frida command
---

## Frida Api for Android

- Java.perform - call the function
- Java.use-use the particular class
     >  var   main = Java.use("sg.vantagepoint.root.MainActivity");

---
- .implementation-override the existing function
    >   main.isDeviceRooted.implementation
- .overload-to use polymorphism
       eg -.overload(“datatype”).implementation

---
### Hands on





---
## Task 1 

![t1](https://media.makeameme.org/created/its-a-secret-y0nz9m.jpg)

---
## Task 2

![t2](https://i.kym-cdn.com/entries/icons/original/000/027/581/strange.jpg)
---
## Task 3

![t3](https://i.ytimg.com/vi/Jo_TDvQ9Qyg/maxresdefault.jpg)

---
## Frida Gadget (Hooking Only)

- Find device architecture
- Reverse the app
- Inject frida-gadget.
- Injecting smali hook.
- Add the 	permission
- Repackage the pack
- Sign the apk
---
## Open the gitbook

 follow the Frida-gadget Section
---


---
### References

- https://koz.io/using-frida-on-android-without-root/

- https://securitygrind.com/ssl-pinning-bypass-with-frida-gadget-gadget-injector-py/

- https://www.slideshare.net/ChandrapalBN/pentesting-android-apps-using-frida-beginners

- https://resources.infosecinstitute.com/frida/

- https://www.frida.re/docs/android/

- Frida Telegram group
