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

def legendre(params, t)
	note = 0;

	if params.length > 0
		note += params[0];
	end
	if params.length > 1
		note += params[1]*t;
	end
	if params.length > 2
		note += params[2] *(1.5 * t**2 - 0.5);
	end
	if params.length > 3
		note += params[3]*( 2.5 * t**3 - 1.5 * t );
	end
	if params.length > 4
		note += params[4]*( 4.375 * t**4 - 4.75 * t**2 + 0.375 );
	end
	if params.length > 5
		note += params[5]*( 7.875 * t**5 - 8.75 * t**3 + 1.875 * t );
	end
		
	return note.floor;
end

def convert(in_file, duration)
	f = File.open(in_file, "r")
	
	solutions = Array.new;
	f.each_line do |line|
		tmp = line.split(";");
		if(!tmp[0].eql?"file") 
			# file;seed;algorithm;offset;startTime;endTime;fitness;evaluations;points;solution
			sol = Solution.new(tmp[3].to_f, tmp[8].to_f, tmp[9]);
			solutions.push(sol);
		end
	end

	# sort the solutions by offset (ASC)
	solutions = solutions.sort! {|x,y| x.offset<=>y.offset}
	
	solutions.each do |sol|
		#puts "#{sol.offset}"
		accum = 0;
		for i in 1..sol.points
			accum += duration;
			note = legendre(sol.params, accum);
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
	end.parse!

	if options.rate.nil?
		rate = 0.002902494;
	else
		rate = options.rate;
	end

	if !options.in_file.nil? 
		convert(options.in_file, rate)
	else
		puts "use '-h' or '--help' for more information";
	end
end
