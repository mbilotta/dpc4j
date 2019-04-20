# Default Plugin Collection for Julia (DPC4J)

Minimal set of plugins to start exploring fractals with [Julia](https://github.com/mbilotta/julia).

## Contents

Currently DPC4J provides 2 number factories, 3 formulas and 4 representations.

### Number factories

* __Double__ provides IEEE 754 64-bit floating point calculations. Functions like sine, cosine, exponential, etc. are computed simply using their counterpart methods in the `java.lang.Math` class.
* __BigDecimal__ provides arbitrary precision calculations by means of the `java.math` package from the standard library. To provide the missing pieces, I brought in classes from Richard J. Mathar's own work, [A Java Math.BigDecimal Implementation of Core Mathematical Functions](http://arxiv.org/abs/0908.3030v2).

### Formulas

* __Quadratic__ is the well-known Mandelbrot recurrence relation:&nbsp;&nbsp;![equation](http://latex.codecogs.com/svg.latex?z_{n%2B1}%20%3D%20z_n^2%20%2B%20c)
* __Multibrot__ is a generalization of the previous formula where the exponent can be set to an arbitrary integer.
* __Carlson__ arise from the Newton's method applied to a particular quartic equation. It's named after [Paul W. Carlson](http://departments.fmarion.edu/mathematics/museum/author.html) who used this formula to illustrate [two artistic orbit trap rendering methods](http://dx.doi.org/10.1016/S0097-8493(99)00123-5).

### Representations

* __Escape Time__ TODO
* __MuEncy__ TODO
* __Tangent Circles__ and __Ring Segments__ TODO.

## Installation

TODO

## Building DPC4J

TODO

## Licensing information

DPC4J is provided under the terms of the GNU General Public License (GPL), ver. 3. Once you’ve installed the package, look at file ~/.juliafg/doc/org/altervista/mbilotta/COPYING for full license terms.

This program is distributed in the hope that will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILIY or FITNESS FOR A PARTICULAR PURPOSE. For more details, refer to the specific license.