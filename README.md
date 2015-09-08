
# yaml-transformer
A Java library for accomplishing simple transformations in Yaml documents.

Building it
-----------
Just checkout the project and generate an [uber jar](http://stackoverflow.com/questions/11947037/what-is-an-uber-jar "What is an uber jar?") with:

```
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```

The jar will be generated in: 

```
target/yaml-transformer-xxx-jar-with-dependencies.jar
```

(rename it as you wish)

Executing it
------------

Execute the jar passing the following arguments:
```
java -jar yaml-transformer.jar <INPUT_FILE> <OUTPUT_FILE> <PROPERTIES_MAP> <OUTPUT_STYLE>
```

Where:

```<PROPERTIES_MAP>``` is a list of the form ```[k1->v1|k2->v2|...]```.
Each ```key->value``` pair describes the new value of a property.

```<OUTPUT_STYLE>``` is an optional argument. It can either be *AUTO*, *BLOCK* or *FLOW*.
Please see the [SnakeYaml documentation](https://code.google.com/p/snakeyaml/wiki/Documentation "SnakeYaml Documentation") for details.


Examples
--------

Considere the following sample file (extracted from the SnakeYaml documentation):

```
invoice: 34843
date: 2001-01-23T00:00:00Z
billTo: &id001
  given: Chris
  family: Dumars
  address:
    lines: |
      458 Walkman Dr.
      Suite #292
    city: New City
    state: MI
    postal: 48046
shipTo: *id001
product:
- sku: BL394D
  quantity: 4
  description: Basketball
  price: 450.0
- sku: BL4438H
  quantity: 1
  description: Super Hoop
  price: 2392.0
tax: 251.42
total: 4443.52
comments: Late afternoon is best. Backup contact is Nancy Billsmer @ 338-4338.
```

If you would like to change the name of the city in the billing address you can execute:

```
java -jar yaml-transformer.jar test.yaml test.out.yaml "root.billTo.address.city->New City"
```

Note that the output style is optional. If you want to specify it, the following command is also valid:

```
java -jar yaml-transformer.jar test.yaml test.out.yaml "root.billTo.address.city->New City" BLOCK
```

In order to execute more than one transformation in the same execution, just surround the list of changes between square brackets and separate each change with the pipe character (```|```).

The following command changes both the name of the city and the state in the billing address of the previous example:

```
java -jar yaml-transformer.jar test.yaml test.out.yaml "[root.billTo.address.city->New City|root.billTo.address.state->New State]"
```

Limitations
-----------

- Only simple map properties can be modified in the current version.
- The input must fully respect the Yaml specification.
- The underlying YAML parser employed by YamlTransformer does not keep existing comments.
- The output file may have a different layout than the input file.


License
-------

yaml-transformer is open source, distributed under the terms of this [license](https://github.com/sergio-castro/yaml-transformer/blob/master/LICENSE "yaml-transformer license").
