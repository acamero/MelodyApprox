#!/usr/bin/env ruby

#
# @author Andrés Camero Unzueta
#
# Execute the program sonic-annotator using a given VAMP plugin over a directory (including
# sub-directories).
#

require 'optparse'
require 'ostruct'

def parseDirectory( inDir, outDir, jar, algorithm) 
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
					puts 'java -jar '+jar+' --file-name '+ inFile +' --pitch-midi --parse-melodia ' + algorithm + ' --out-dir ' + outDir
					system('java -jar '+jar+' --file-name '+ inFile +' --pitch-midi --parse-melodia ' + algorithm + ' --out-dir ' + outDir)
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
		opt.on('-a', '--method PARSE_METHOD', 'Name of the method to parse melody') { |o| options.algorithm = o }
		opt.on('-j', '--jar JAR_PATH', 'Path of the jar') { |o| options.jar = o }
	end.parse!
	
	if !options.in_dir.nil?	&& !options.out_dir.nil? && !options.algorithm.nil? && !options.jar.nil?
		if File.file?(options.jar) 
			parseDirectory(options.in_dir, options.out_dir, File.absolute_path(options.jar), options.algorithm);		
		else
			puts "Could not find jar '#{options.jar}'";
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
