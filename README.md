# parent-spring-boot-starter
### Spring boot starters kit
#### Provided:
* aop
    * methodResultAspectLog (bean)
    * methodDurationAspectLog (bean)
    * LogMethodDuration (annotation)
    * LogMethodResult (annotation)
* core 
    * BeansCollection (interface)
    * AbstractBeansCollection (abstract class)
* json
    * JsonConverter (interface)
    * jacksonJsonConverter (bean)
    * objectMapper (bean)
* random
    * Randomizer (interface)
    * AbstractRandomizer (abstract class)
    * Verifier (functional interface)
    
    * booleanBoundedTyped (bean)
    * dateBoundedType (bean)
    * doubleBoundedType (bean)
    * longBoundedType (bean)
    * stringBoundedType (bean)
    * boundedBeansCollection (bean)
    * *
    * booleanRandomizer (bean)
    * dateRandomizer (bean)
    * doubleRandomizer (bean)  
    * longRandomizer (bean)
    * stringRandomizer (bean)
    * randomizerBeansCollection (bean)
    *  *
    * specificationVerifier (bean)
    * genericRandomizerFactory (bean)
    * genericCollectionRandomizer (bean)
    * *
    * Specification (class)
* stream
    * streamUtils (bean)
* test
    * AbstractProvider (abstract class)
    * AbstractDoubleProvider (abstract class)
    * AbstractLongProvider (abstract class)
    * AbstractStringProvider (abstract class)
* web
    * typedResponseWrapperRestControllerAdvice (bean)
    * TypedResponseWrapperRestController (annotation)