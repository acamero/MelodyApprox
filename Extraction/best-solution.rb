#!/usr/bin/env ruby

#
# @author Andrés Camero Unzueta
#
# Execute the program sonic-annotator using a given VAMP plugin over a directory (including
# sub-directories).
#

require 'optparse'
require 'ostruct'

def getBestSolution(inFile, outFile)
	solution = Hash.new;
	fitness = Hash.new
	f = File.open(inFile, "r") 
	f.each_line do |line|
		dtl = line.split(";");
		key = dtl[0] + ";" + dtl[2] + ";" + dtl[3];
		if fitness[key].nil? || fitness[key].to_f>dtl[6].to_f
			fitness[key] = dtl[6];
			solution[key] = line;			
		end
	end
	
	begin
		out = File.open(outFile, "w")
		solution.each_key { |key| out.write(solution[key]) }
	rescue IOError => e
		#some error occur, dir not writable etc.
		puts "error while writting to file #{outFile}"
	ensure
		out.close unless out.nil?
	end
end

def parseDirectory( inDir, outDir) 
	# create out directory if not exists
	Dir.mkdir(outDir) unless File.exists?(outDir)
	outDir = File.absolute_path(outDir);
	
	# validate that "in" directory exists
	if File.directory?(inDir)	
		# get folders
		Dir.chdir(inDir)
		dirs = Dir['**/*']	
		absDir = Dir.getwd

		if dirs.nil?
			puts "Fail to open directory '#{inDir}'"
		elsif dirs.respond_to?("each")
			# check if it is a file
			dirs.each do |inFile|			
				if File.file?(inFile) && inFile.end_with?("detail.csv")
					outFile = outDir+"/"+inFile.sub("detail.csv","best.csv");
					# parse solutions
					getBestSolution(inFile,outFile);
				end
			end
		else	
			# Weird error
			puts "Something unexpected occurred while opening directory '#{inDir}'";
		end	
	else
		puts "Directory '#{inDir}' not found";
	end
end


if __FILE__ == $0
	options = OpenStruct.new
	OptionParser.new do |opt|
		opt.on('-d', '--in-dir IN_DIR', 'Directory containing solutions to parse') { |o| options.in_dir = o }
		opt.on('-f', '--in-file IN_FILE', 'File containing solutions to parse') { |o| options.in_file = o }
		opt.on('-o', '--out-dir OUT_DIR', 'Directory to store best solutions') { |o| options.out_dir = o }
	end.parse!
	
	if !options.in_dir.nil?	&& !options.out_dir.nil?
		parseDirectory(options.in_dir, options.out_dir);	
	elsif !options.in_file.nil? && !options.out_dir.nil?
		if File.file?(options.in_file) && options.in_file.end_with?("detail.csv")
			# create out directory if not exists
			Dir.mkdir(options.out_dir) unless File.exists?(options.out_dir)
			outDir = File.absolute_path(options.out_dir);
			outFile = outDir+"/"+options.in_file.sub("detail.csv","best.csv");
			puts outFile;
			getBestSolution(options.in_file,outFile);
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
