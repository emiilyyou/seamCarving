Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

In order to find the horiziontal or vertical seams, we utilized dynamic
programming by calculating the energy of each pixel and updating the distTo,
edgeTo, and energy arrays as we traverse and relax the picture. To implement
dynamic programming, we considered two cases to traverse through the paths, namely
when j was not equal to 0/width - 1, considering when to cross the left bottom,
direct bottom, and right bottom pixels. We also developed DP recurrence using a
private helper method called relax that would update our distTo and edgeTo arrays
as adapted from the picture API. At the end, we find the minimum energy path
across the bottom row to the top row, store it in an array and return it.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

For an image to be suitable to the seam-carving approach, we would need a four-sided
picture with the same widths across the top and bottom and the same heights across
the left and right, so the picture's width and height is consistent in the image.
An image that would not work well would when the image is not a square or rectangle
because seam removal would be difficult. Additonally, an image that would not
work well is one with high energy throughout the entire image, making the
find function unable to find a seam with the smallest energy.


/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
400             0.25               -            -
800             0.466             1.864       0.898
1600            0.832             1.785       0.836
3200            1.809             2.174       1.121
6400            3.821             2.112       1.079
12800           7.603             1.989       0.992
25600           19.834            2.609       1.383
51200           48.82             2.461       1.299



(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
400             0.253               -           -
800             0.443             1.751       0.808
1600            0.79              1.783       0.834
3200            1.535             1.943       0.958
6400            3.39              2.208       1.143
12800           6.91              2.038       1.027
25600           16.171            2.340       1.227
51200           47.078            2.911       1.542



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


   3.5 x 10^-6  * W^1.3 * H^1.5
       _______________________________________

    T(n) = a * W^1.3 * H^1.5
    47.078 = a * 51200^1.3 * 2000^1.5
    47.078 = a * 149.51 * 89442.72
    a = 3.5 x 10^-6

To determine the formula for the running time, we used the log ratios as
the b value in T(n) = a x b^n for the W and H variables. Then, we used 51200 as
our W and 2000 as H (as it is constant) with the running time of 47.078 to solve
for an a-value of 3.5 x 10^-6.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

N/A

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

We were unsure of which shortest path algorithm to utilize in order to create
the most efficient program. However, we realized that using dynamic programming
would be a good way, so we were able to figure it out.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
Progra
