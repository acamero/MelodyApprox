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
				puts "\ti2 #{start} #{accum} 0.9 #{prevMidi}"; 
			end
			start = tmp[0];
			accum = 0;
		end
		
		prevTime = tmp[0];
		prevMidi = midi;
	end
	f.close
end

def putHeader(fileName) 
	puts "<CsoundSynthesizer>
<CsOptions>
	; Select audio/midi flags here according to platform
	 -odac    ;;;realtime audio out
	;-iadc    ;;;uncomment -iadc if realtime audio input is needed too
	; For Non-realtime ouput leave only the line below:
	; -o #{fileName}.wav -W ;;; for file output any platform
</CsOptions>

<CsInstruments>
	; originally tone.orc 
	sr = 44100
	;kr = 4410
	ksmps = 32
	nchnls = 1
	0dbfs = 1

	instr   1 
	; simple oscillator 
	; p4=amp
	; p5=pitch
	a1 oscil p4, cpsmidinn(p5), 1 
	 out a1
	endin

	instr 2
	; p4=amp
	; p5=freq
	; p6=attack time
	; p7=release time
	ares linen  p4, p3*0.1, p3, p3*0.1 ; p4, p6, p3, p7 
	asig poscil ares, cpsmidinn(p5), 1    
	     outs   asig, asig   		                                 
	endin
</CsInstruments>

<CsScore>
	f1 0 16384 10 1                                          ; Sine
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
			outFile = options.in_file;
			i = options.in_file.rindex("/");
			if i
				outFile = outFile[i+1,outFile.length];
			end

			putHeader(outFile);
			convertToCsd(options.in_file, options.is_midi);	
			putFooter();	
		else
			puts "Could not find file '#{options.in_file}'";
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
