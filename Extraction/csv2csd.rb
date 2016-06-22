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

def convertToCsd(in_file, is_midi, min_note) 
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
			if prevMidi>0 && accum.round(3)>=min_note
				puts "\ti1 #{start.round(3)} #{accum.round(3)} 75 #{prevMidi}"; 
			end
			start = tmp[0];
			accum = 0;
		end
		
		prevTime = tmp[0];
		prevMidi = midi;
	end
	if prevMidi>0 && accum.round(3)>=0.005
		puts "\ti1 #{start.round(3)} #{accum.round(3)} 75 #{prevMidi}"; 
	end
	f.close
end

def putHeader(fileName, isMidi) 
	#if(isMidi) 
	#	freq = "cpsmidinn(p5)";
	#else
	#	freq = "p5";
	#end
	freq = "cpsmidinn(p5)";

	puts "<CsoundSynthesizer>
<CsOptions>
	; Select audio/midi flags here according to platform
	; -odac    ;;;realtime audio out
	;-iadc    ;;;uncomment -iadc if realtime audio input is needed too
	; For Non-realtime ouput leave only the line below:
	 -o #{fileName}.wav -W ;;; for file output any platform
</CsOptions>

<CsInstruments>
	; originally tone.orc 
	sr = 44100
	;kr = 4410
	ksmps = 1
	nchnls = 1
	
	instr   1
	  ;                ihold                           ;turn on note indefinitely
	  idur    =       abs(p3)
	  ibase   =       cpsmidinn(p5)              ;p4 is keyboard pitch
	  iroct   =       octmidinn(p5)
	  irbase  =       octpch(4.09)            ;base of rate scl table
	  irrange =       octpch(13.06)-irbase
	  iveloc  =       p4                      ;0 <= p5 <= 127
	  iop1fn  =       12                     ;param tables for ops
	  iop2fn  =       13
	  iop3fn  =       14
	  iop4fn  =       15
	  iop5fn  =       16
	  iop6fn  =       17
	  iampfn  =       2                     ;amp/level map function
	  ipkamp  =       4000                     ;scale for converter
	  irsfn   =       3                     ;rate scaling function
	  idevfn  =       8                     ;level/pkdev map func
	  irisefn =       4                     ;eg rise rate fn
	  idecfn  =       6                     ;eg decay rate fn
	  ivsfn   =       10                     ;vel sensitivity fn
	  ivelfn  =       9                     ;vel/amp fac map fn
	  iveloc  table   iveloc,ivelfn           ;map this note's veloc
	  ifeedfn =       11
	  ifeed   table   7,ifeedfn             ;0 <= p25 <= 7 (feedbk)
	  ifeed   =       ifeed/(2 * 3.14159)     ;dev in radians
	  idetfac =       4                       ;max detuning divisor
	  imap128 =       127/99                  ;mapping constant 99->127
	  irscl   table   (iroct-irbase)/irrange*127,irsfn
	  irscl   =       irscl*6
	  iop     =       1                       ;start loop with op1
	  iopfn   =       iop1fn

	loop:
	;---------------------------------read operator parameters
		ilvl    table   0,iopfn
		ivel    table   1,iopfn
		iegr1   table   2,iopfn
		iegr2   table   3,iopfn
		iegr3   table   4,iopfn
		iegr4   table   5,iopfn
		iegl1   table   6,iopfn
		iegl2   table   7,iopfn
		iegl3   table   8,iopfn
		iegl4   table   9,iopfn
		iams    table   10,iopfn
		imode   table   11,iopfn
		ifreq   table   12,iopfn
		idet    table   13,iopfn
		irss    table   14,iopfn
	;----------------------------------initialize operator
		ihz     =       (imode > 0 ? ifreq : ibase * ifreq) + idet/idetfac
	 iamp	=	ilvl/99		;rescale to 0 -> 1
		ivfac   table   ivel,ivsfn

		iegl1   =       iamp*iegl1
		iegl2   =       iamp*iegl2
		iegl3   =       iamp*iegl3
		iegl4   =       iamp*iegl4

		iegl1   =       iegl1*(1-ivfac)+iegl1*ivfac*iveloc
		iegl2   =       iegl2*(1-ivfac)+iegl2*ivfac*iveloc
		iegl3   =       iegl3*(1-ivfac)+iegl3*ivfac*iveloc
		iegl4   =       iegl4*(1-ivfac)+iegl4*ivfac*iveloc

		irs     =       irscl*irss
		iegr1   =       (iegr1+irs > 99 ? 99 : iegr1+irs)
		iegr2   =       (iegr2+irs > 99 ? 99 : iegr2+irs)
		iegr3   =       (iegr3+irs > 99 ? 99 : iegr3+irs)
		iegr4   =       (iegr4+irs > 99 ? 99 : iegr4+irs)

		irfn    =       (iegl1 > iegl4 ? irisefn : idecfn)
		iegd1   table   iegr1,irfn               ;convert rate->dur
		ipct1   table   iegl4,irfn+1             ;pct fn is next one
		ipct2   table   iegl1,irfn+1
		iegd1   =       abs(iegd1*ipct1-iegd1*ipct2)
		iegd1   =       (iegd1 == 0 ? .001 : iegd1)

		irfn    =       (iegl2 > iegl1 ? irisefn : idecfn)
		iegd2   table   iegr2,irfn
		ipct1   table   iegl1,irfn+1
		ipct2   table   iegl2,irfn+1
		iegd2   =       abs(iegd2*ipct1-iegd2*ipct2)
		iegd2   =       (iegd2 == 0 ? .001 : iegd2)

		irfn    =       (iegl3 > iegl2 ? irisefn : idecfn)
		iegd3   table   iegr3,irfn
		ipct1   table   iegl2,irfn+1
		ipct2   table   iegl3,irfn+1
		iegd3   =       abs(iegd3*ipct1-iegd3*ipct2)
		iegd3   =       (iegd3 == 0 ? .001 : iegd3)

		iegd4   table   iegr4,idecfn
		        if      (iegl3 <= iegl4) igoto continue
		ipct1   table   iegl3,irfn+1
		ipct2   table   iegl4,irfn+1
		iegd4   =       abs(iegd4*ipct1-iegd4*ipct2)
		iegd4   =       (iegd4 == 0 ? .001 : iegd4)
	continue:
		        if      (iop > 1) igoto op2
	op1:
		i1egd1  =       iegd1
		i1egd2  =       iegd2
		i1egd3  =       iegd3
		i1egd4  =       iegd4
		i1egl1  =       iegl1
		i1egl2  =       iegl2
		i1egl3  =       iegl3
		i1egl4  =       iegl4
		i1ams   =       iams
		i1hz    =       ihz
		iop     =       iop + 1
		iopfn   =       iop2fn
		        igoto   loop

	op2:            if      (iop > 2) igoto op3
		i2egd1  =       iegd1
		i2egd2  =       iegd2
		i2egd3  =       iegd3
		i2egd4  =       iegd4
		i2egl1  =       iegl1
		i2egl2  =       iegl2
		i2egl3  =       iegl3
		i2egl4  =       iegl4
		i2ams   =       iams
		i2hz    =       ihz
		iop     =       iop + 1
		iopfn   =       iop3fn
		        igoto   loop

	op3:            if      (iop > 3) igoto op4
		i3egd1  =       iegd1
		i3egd2  =       iegd2
		i3egd3  =       iegd3
		i3egd4  =       iegd4
		i3egl1  =       iegl1
		i3egl2  =       iegl2
		i3egl3  =       iegl3
		i3egl4  =       iegl4
		i3ams   =       iams
		i3hz    =       ihz
		iop     =       iop + 1
		iopfn   =       iop4fn
		        igoto   loop

	op4:            if      (iop > 4) igoto op5
		i4egd1  =       iegd1
		i4egd2  =       iegd2
		i4egd3  =       iegd3
		i4egd4  =       iegd4
		i4egl1  =       iegl1
		i4egl2  =       iegl2
		i4egl3  =       iegl3
		i4egl4  =       iegl4
		i4ams   =       iams
		i4hz    =       ihz
		iop     =       iop + 1
		iopfn   =       iop5fn
		        igoto   loop

	op5:            if      (iop > 5) igoto op6
		i5egd1  =       iegd1
		i5egd2  =       iegd2
		i5egd3  =       iegd3
		i5egd4  =       iegd4
		i5egl1  =       iegl1
		i5egl2  =       iegl2
		i5egl3  =       iegl3
		i5egl4  =       iegl4
		i5ams   =       iams
		i5hz    =       ihz
		iop     =       iop + 1
		iopfn   =       iop6fn
		        igoto   loop

	op6:
		i6egd1  =       iegd1
		i6egd2  =       iegd2
		i6egd3  =       iegd3
		i6egd4  =       iegd4
		i6egl1  =       iegl1
		i6egl2  =       iegl2
		i6egl3  =       iegl3
		i6egl4  =       iegl4
		i6ams   =       iams
		i6hz    =       ihz
	;=====================================================================
		        timout  idur,999,final          ;skip during final decay
		k1sus   linseg  i1egl4,i1egd1,i1egl1,i1egd2,i1egl2,i1egd3,i1egl3,1,i1egl3
		k2sus   linseg  i2egl4,i2egd1,i2egl1,i2egd2,i2egl2,i2egd3,i2egl3,1,i2egl3
		k3sus   linseg  i3egl4,i3egd1,i3egl1,i3egd2,i3egl2,i3egd3,i3egl3,1,i3egl3
		k4sus   linseg  i4egl4,i4egd1,i4egl1,i4egd2,i4egl2,i4egd3,i4egl3,1,i4egl3
		k5sus   linseg  i5egl4,i5egd1,i5egl1,i5egd2,i5egl2,i5egd3,i5egl3,1,i5egl3
		k6sus   linseg  i6egl4,i6egd1,i6egl1,i6egd2,i6egl2,i6egd3,i6egl3,1,i6egl3
		k1phs   =       k1sus
		k2phs   =       k2sus
		k3phs   =       k3sus
		k4phs   =       k4sus
		k5phs   =       k5sus
		k6phs   =       k6sus
		        kgoto   output
	final:
		k1fin   linseg  1,i1egd4,0,1,0
		k1phs   =       i1egl4+(k1sus-i1egl4)*k1fin
		k2fin   linseg  1,i2egd4,0,1,0
		k2phs   =       i2egl4+(k2sus-i2egl4)*k2fin
		k3fin   linseg  1,i3egd4,0,1,0
		k3phs   =       i3egl4+(k3sus-i3egl4)*k3fin
		k4fin   linseg  1,i4egd4,0,1,0
		k4phs   =       i4egl4+(k4sus-i4egl4)*k4fin
		k5fin   linseg  1,i5egd4,0,1,0
		k5phs   =       i5egl4+(k5sus-i5egl4)*k5fin
		k6fin   linseg  1,i6egd4,0,1,0
		k6phs   =       i6egl4+(k6sus-i6egl4)*k6fin



	;--------------------Algorithm 9----------------------------------;
		        ;if      (k1fin + k3fin) > 0 kgoto output
		        ;turnoff                 ;when carrier oscil(s) done, turn off.
	output:                  
		k1gate  tablei  k1phs,iampfn    ;use ampfn for any carrier
		k2gate  tablei  k2phs,idevfn    ;use devfn for any modulator
	 	k3gate	tablei	k3phs,iampfn
	 	k4gate	tablei	k4phs,idevfn
	 	k5gate	tablei	k5phs,idevfn
	 	k6gate	tablei	k6phs,idevfn
		     
		a6sig   oscili  k6gate,i6hz,1

		a5sig   oscili  k5gate,i5hz,1

		a4phs   phasor  i4hz
		a4sig   tablei  a4phs+a5sig,1,1,0,1
		a4sig   =       a4sig*k4gate

		a3phs   phasor  i3hz
		a3sig   tablei  a3phs+a4sig+a6sig,1,1,0,1
		a3sig   =       a3sig*k3gate
	  
		a2sig   init    0               ;initialize for feedback
		a2phs   phasor  i2hz
		a2sig   tablei  a2phs+a2sig*(.4*ifeed),1,1,0,1
		a2sig   =       a2sig*k2gate

		a1phs   phasor  i1hz
		a1sig   tablei  a1phs+a2sig,1,1,0,1
		a1sig   =       a1sig*k1gate
		        out     (a1sig+a3sig)*ipkamp
	endin
</CsInstruments>

<CsScore>
	f01     0       512     10      1
	; operator fpoutput level to amp scale function (data from Chowning/Bristow)
	f02     0       128     7       0       10      .003    10      .013
		10      .031    10      .079    10      .188    10      .446
		5       .690    5       1.068   5       1.639   5       2.512
		5       3.894   5       6.029   5       9.263   4       13.119
		29      13.119
	; rate scaling function
	f03     0       128     7       0       128     1
	; eg rate rise function for lvl change between 0 and 99 (data from Opcode)
	f04     0       128     -7      38      5       22.8    5       12      5
		7.5     5       4.8     5       2.7     5       1.8     5       1.3
		8       .737    3       .615    3       .505    3       .409    3
		.321    6       .080    6       .055    2       .032    3       .024
		3       .018    3       .014    3       .011    3       .008    3
		.008    3       .007    3       .005    3       .003    32      .003
	; eg rate rise percentage function
	f05     0       128     -7      .00001  31      .00001  4       .02     5
		.06     10      .14     10      .24     10      .35     10      .50
		10      .70     5       .86     4       1.0     29      1.0
	; eg rate decay function for lvl change between 0 and 99
	f06     0       128     -7      318     4       181     5       115     5
		63      5       39.7    5       20      5       11.2    5       7
		8       5.66    3       3.98    6       1.99    3       1.34    3
		.99     3       .71     5       .41     3       .15     3       .081
		3       .068    3       .047    3       .037    3       .025    3
		.02     3       .013    3       .008    36      .008
	; eg rate decay percentage function
	f07     0       128     -7      .00001  10      .25     10      .35     10
		.43     10      .52     10      .59     10      .70     10      .77
		10      .84     10      .92     9       1.0     29      1.0
	; eg level to peak deviation mapping function (index in radians = Index / 2PI)
	f08     0       128     -7      0       10      .000477 10      .002
		10      .00493  10      .01257  10      .02992  10      .07098
		5       .10981  5       .16997  5       .260855 5       .39979
		5       .61974  5       .95954  5       1.47425 4       2.08795
		29      2.08795
	; velocity to amp factor mapping function (rough guess)
	f09     0       129     9       .25     1       0
	; velocity sensitivity scaling function
	f10     0       8       -7      0       8       1
	; feedback scaling function
	f11     0       8       -7      0       8       7
	f12   0    32    -2    99    3   62   21   7   44   
		99   79   0   0   
		2   0   1.000000   -4   
		7   
	f13   0    32    -2    72    1   66   8   0   39   
		98   79   0   0   
		0   0   1.010000   -7   
		3   
	f14   0    32    -2    99    3   62   21   7   44   
		99   79   0   0   
		2   0   1.000000   1   
		7   
	f15   0    32    -2    87    2   78   73   51   55   
		99   58   0   0   
		0   1   123.000000   -1   
		1   
	f16   0    32    -2    89    4   69   22   13   42   
		99   88   0   0   
		0   0   1.000000   4   
		1   
	f17   0    32    -2    63    0   67   11   0   42   
		99   94   0   0   
		0   0   5.000000   0   
		6  
"
end

def putFooter() 
	puts "
	e
</CsScore>

</CsoundSynthesizer>
";
end

if __FILE__ == $0
	options = OpenStruct.new
	OptionParser.new do |opt|
		opt.on('-f', '--file IN_FILE', 'File containing "melodia" in csv format') { |o| options.in_file = o };
		opt.on('-m', '--midi', 'File is encoded in MIDI standard notation') { |o| options.is_midi = o };
		opt.on('-t', '--min-note TIME', 'Select the minimum note duration') { |o| options.min_note = o.to_f };
	end.parse!

	if options.min_note.nil? 
		min_note = 0.005;
	else
		min_note = options.min_note;
	end
	
	if !options.in_file.nil?
		if File.file?(options.in_file) 
			outFile = options.in_file;
			i = options.in_file.rindex("/");
			if i
				outFile = outFile[i+1,outFile.length];
			end

			putHeader(outFile, options.is_midi);
			convertToCsd(options.in_file, options.is_midi, min_note);	
			putFooter();	
		else
			puts "Could not find file '#{options.in_file}'";
		end
	else
		puts "use '-h' or '--help' for more information";
	end
end
