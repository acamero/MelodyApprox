#!/usr/bin/env ruby

#
# @author Andrés Camero Unzueta
#

require 'optparse'
require 'ostruct'

class Solution
	@offset;
	@points;
	@params;
	def offset
		@offset
	end
	def points
		@points
	end	
	def params
		@params
	end

	def initialize(offset, points, solution)
		@offset = offset;
		@points = points;
		@params = solution;
		@params = @params.sub("[","");
		@params = @params.sub("]","");
		@params = @params.split(",").map( &:to_f );
	end 
end

def deg2Rad(degree)
	return degree*Math::PI/180;
end

def polyTri(params, t, omega)
	note = 0;
	note += params[0];
	note += params[1]*t;
	note += params[2] * t**2;
	
	n = (params.length - 3)/2;

	for i in 3..(3+n-1)
		note += params[i] * Math.sin(deg2Rad((i-2)*omega*t));
	end	

	for i in (3+n)..(params.length-1)
		note += params[i] * Math.cos(deg2Rad((i-3-n)*omega*t));
	end
			
	return note.round;
end

def convert(in_file, duration, omega)
	f = File.open(in_file, "r")
	
	solutions = Array.new;
	f.each_line do |line|
		tmp = line.split(";");
		# file;seed;algorithm;offset;startTime;endTime;fitness;evaluations;points;solution
		sol = Solution.new(tmp[3].to_f, tmp[8].to_f, tmp[9]);
		solutions.push(sol);
	end

	# sort the solutions by offset (ASC)
	solutions = solutions.sort! {|x,y| x.offset<=>y.offset}
	
	solutions.each do |sol|
		#puts "#{sol.offset}"
		accum = 0;
		for i in 1..sol.points
			accum += duration;
			note = polyTri(sol.params, accum, omega);
			time = sol.offset + accum;
			puts "#{time}, #{note}";
		end
	end
	f.close
end

if __FILE__ == $0
	options = OpenStruct.new
	OptionParser.new do |opt|
		opt.on('-f', '--file IN_FILE', 'File containing "melodia" in csv format') { |o| options.in_file = o }
		opt.on('-r', '--samp-rate RATE', 'Duration of each note') { |o| options.rate = o.to_f }
		opt.on('-o', '--omega OMEGA', 'Omega') { |o| options.omega = o.to_f }
	end.parse!

	if options.rate.nil? 
		rate = 0.002902494;
	else
		rate = options.rate;
	end

	if !options.in_file.nil? && !options.omega.nil?
		convert(options.in_file, rate, options.omega)
	else
		puts "use '-h' or '--help' for more information";
	end
end
