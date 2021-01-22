<p align="center"><img src="https://craftinginterpreters.com/image/logotype-small.png"/></p>
 
# Lox

![GitHub Repo stars](https://img.shields.io/github/stars/okaabe/void?color=orange&style=for-the-badge)
![GitHub issues](https://img.shields.io/github/issues/okaabe/void?color=orange&style=for-the-badge)
![GitHub last commit](https://img.shields.io/github/last-commit/okaabe/void?color=orange&style=for-the-badge)


<p>My implementation of the programming language created in craftinginterpreters book using kotlin instead of java</p>

# Syntax

### Conditions(if && else)
```js
var name = "okaabe"
if (name == "okaabe") {
 println(0)
} else println(1)
```
### Functions
```js
fn add(a, b) {
 return a + b
}

println(add(5, 10))//15
```
### Classes
```js
class Animal {
  fn init(type, name) {
    this.type = type
    this.name = name
  }
  
  fn say() {
   println("[" + this.type + "] " + this.name)
  }
}

class Dog {
 fn init(name) {
  super("dog", name)
 }
}

var dog = Dog("123")

dog.say()//[dog] 123
```

### To Do
- [ ] Tree walk interpreter
- [ ] A bytecode virtual machine

### Links

<table>
  <tr>
    <td align="center"><a href="https://github.com/munificent/craftinginterpreters"><img src="https://avatars0.githubusercontent.com/u/46275?s=400&v=44" width="100px;" alt=""/><br /><sub><b>Craftinginterpreter author</b></sub></a>
