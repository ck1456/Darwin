#!/bin/bash

datadir="data/"
solndir="soln/"
invoke="java -jar GenMat.jar"
#Problem 1
for n in darwin 1 2 3 
do
  for type in B
  do
    infile="${datadir}input_${n}.txt"
    outfile="${solndir}output_${n}.txt"
    echo "${type}: ${infile}"
    ${invoke} ${type} ${infile} ${outfile}

    java -jar ./spec/grader.jar ${type} "${infile}" "${outfile}"
  done
done
echo

