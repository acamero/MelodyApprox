#!/usr/bin/env ruby

#
# @author Andrés Camero Unzueta
#
# Execute the program sonic-annotator using a given VAMP plugin over a directory (including
# sub-directories).
#

require 'optparse'
require 'ostruct'

def parseDirectory( inDir, outDir, jar, algorithm, num) 
	# create out directory if not exists
	Dir.mkdir(outDir) unless File.exists?(outDir)
	outDir = File.absolute_path(outDir);
	
	# validate that in directory exists
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
				if File.file?(inFile) 
					for i in 1..num
						puts 'java -jar '+jar+' --file-name '+ inFile +' --pitch-midi --algorithm ' + algorithm + ' --out-dir ' + outDir + " --seed #{i}" 
						system('java -jar '+jar+' --file-name '+ inFile +' --pitch-midi --algorithm ' + algorithm + ' --out-dir ' + outDir + " --seed #{i}" )						
					end
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
		opt.on('-d', '--in-dir IN_DIR', 'Directory containing music to parse') { |o| options.in_dir = o }
		opt.on('-o', '--out-dir OUT_DIR', 'Directory to store pitch contours') { |o| options.out_dir = o }
		opt.on('-a', '--algorithm ALGORITHM', 'Name of the algorithm to be used') { |o| options.algorithm = o }
		opt.on('-j', '--jar JAR_PATH', 'Path of the jar') { |o| options.jar = o }
		opt.on('-n', '--num-repetitions NUMBER', 'Number of repetitions') { |o| options.num = o }
	end.parse!
	
	if !options.in_dir.nil?	&& !options.out_dir.nil? && !options.algorithm.nil? && !options.jar.nil? && !options.num.nil?
		if File.file?(options.jar) || options.n < 1
			parseDirectory(options.in_dir, options.out_dir, File.absolute_path(options.jar), options.algorithm, options.num.to_i);		
		else
			puts "Could not find jar '#{options.jar}'";
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
