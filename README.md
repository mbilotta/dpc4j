[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Default Plugin Collection for Julia (DPC4J)

Minimal set of plugins to start exploring fractals with [Julia](https://github.com/mbilotta/julia).

## Contents

Currently DPC4J provides 2 number factories, 3 formulas and 4 representations.

### Number factories

* __Double__ provides IEEE 754 64-bit floating point calculations. Functions like sine, cosine, exponential, etc. are computed simply using their counterpart methods in the `java.lang.Math` class.
* __BigDecimal__ provides arbitrary precision calculations by means of the `java.math` package from the standard library. To provide the missing pieces, I brought in classes from [Richard J. Mathar](http://www.mpia.de/~mathar/)'s own work, [A Java Math.BigDecimal Implementation of Core Mathematical Functions](http://arxiv.org/abs/0908.3030v3).

### Formulas

* __Quadratic__ is the well-known Mandelbrot recurrence relation:&nbsp;&nbsp;![equation](http://latex.codecogs.com/svg.latex?z_{n%2B1}%20%3D%20z_n^2%20%2B%20c)
* __Multibrot__ is a generalization of the previous formula where the exponent can be set to an arbitrary integer.
* __Carlson__ arises from the Newton's method applied to a particular quartic equation. It's named after [Paul W. Carlson](http://departments.fmarion.edu/mathematics/museum/author.html) who used this formula to illustrate [two artistic orbit trap rendering methods](http://dx.doi.org/10.1016/S0097-8493(99)00123-5).

### Representations

* __Escape Time__ is probably the first known coloring method. It is also known as _Escape Iterations_ or _Dwell_.
* __MuEncy__ is based on the [algorithm](http://mrob.com/pub/muency/color.html) used by [Robert Munafo](http://mrob.com/) in the [Mu-Ency](http://mrob.com/pub/muency.html) illustrations. 
* __Tangent Circles__ and __Ring Segments__ are those two [orbit trap](https://www.mi.sanu.ac.rs/vismath/javier/b7.htm) rendering methods described by Paul W. Carlson in the same paper linked above.

## Installation

The first time Julia runs, an error message will show:

![Error shown the first time Julia runs](https://user-images.githubusercontent.com/14998549/122667426-2b418f00-d1b3-11eb-9888-920a2bf01d53.png)

Keep this window open and follow these steps:

1. Download the [latest release](https://github.com/mbilotta/dpc4j/releases/latest) of DPC4J (the file named `dpc4j.jup`).
2. Click _Install new plugins..._ in the error dialog.
3. Locate file `dpc4j.jup`.
4. Click _Install_.
5. Restart Julia.

### Installation using the CLI

Run the following:

    java -jar julia.jar install dpc4j.jup

## Update installation

1. Open _File_ → _Install new plugins..._
2. Locate file `dpc4j.jup`.
3. Click _Install_.
4. When you're first asked about a file name conflict, click _Yes to All_.
5. Restart Julia.

### Update installation using the CLI

Run the following:

    java -jar julia.jar install --overwrite-existing dpc4j.jup

## Building DPC4J

You need JDK 1.8+ and Maven installed. You also need to install Julia as a Maven dependency:

    git clone https://github.com/mbilotta/julia.git
    cd julia
    mvn clean install

Then you can build DPC4J as any other Maven project:

    git clone https://github.com/mbilotta/dpc4j.git
    cd dpc4j
    mvn clean package

Finally, to generate the JUP archive, you should use the Julia CLI:
```
java -jar ~/julia.jar package --license COPYING --output dpc4j.jup org/altervista/mbilotta xml/ target/dpc4j.jar
```
Supposing you have placed `julia.jar` in your home directory, running this command from the project root directory will generate `dpc4j.jup`.

## Licensing information

DPC4J is provided under the terms of the GNU General Public License (GPL), ver. 3. Once you’ve installed the package, look at file `~/.juliafg/doc/org/altervista/mbilotta/COPYING` for full license terms.

This program is distributed in the hope that will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILIY or FITNESS FOR A PARTICULAR PURPOSE. For more details, refer to the specific license.
