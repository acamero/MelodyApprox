#!/usr/bin/env ruby

#
# @author Andrés Camero Unzueta
#

require 'optparse'
require 'ostruct'

def freq2Midi(freq) 
	midi = 0;	
	if freq>0
		midi = 69 + 12 * Math.log2(freq/440);
	end 
	return midi.round;
end

def convertToCsd(in_file, is_midi) 
	f = File.open(in_file, "r")
	prevMidi = 0;
	accum = 0;
	start = 0;
	prevTime = 0;

	f.each_line do |line|
		tmp = line.split(",").map( &:to_f );
		midi = tmp[1];
		if !is_midi
			midi = freq2Midi( tmp[1] ); 
		end

		if midi==prevMidi
			accum += tmp[0] - prevTime;
		else
			if prevMidi>0
				puts "\ti1 #{start} #{accum} 2000 #{prevMidi}"; 
			end
			start = tmp[0];
			accum = 0;
		end
		
		prevTime = tmp[0];
		prevMidi = midi;
	end
	f.close
end

def putHeader() 
	puts "<CsoundSynthesizer>
<CsOptions>
	; Select audio/midi flags here according to platform
	 -odac    ;;;realtime audio out
	;-iadc    ;;;uncomment -iadc if realtime audio input is needed too
	; For Non-realtime ouput leave only the line below:
	; -o oscils.wav -W ;;; for file output any platform
</CsOptions>

<CsInstruments>
	; originally tone.orc 
	sr = 44100
	kr = 4410
	ksmps = 10
	nchnls = 1
	instr   1 
	a1 oscil p4, cpsmidinn(p5), 1 ; simple oscillator
	 out a1
	endin
</CsInstruments>

<CsScore>
	f1 0 8192 10 1
"
end

def putFooter() 
	puts "
	e
</CsScore>

</CsoundSynthesizer>
"
end

if __FILE__ == $0
	options = OpenStruct.new
	OptionParser.new do |opt|
		opt.on('-f', '--file IN_FILE', 'File containing "melodia" in csv format') { |o| options.in_file = o };
		opt.on('-m', '--midi', 'File is encoded in MIDI standard notation') { |o| options.is_midi = o };
	end.parse!
	
	if !options.in_file.nil?
		if File.file?(options.in_file) 
			putHeader();
			convertToCsd(options.in_file, options.is_midi);	
			putFooter();	
		else
			puts "Could not find file '#{options.in_file}'";
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
