function fit = fitnessByPhrases(filePath)

rawin = csvread (filePath);

% get just the positive frequencies
rawin(:,3)=max(rawin(:,2),0);
% calculate the midi pitch
rawin(:,4) = floor(12*log2(rawin(:,3)/440) + 69);
% remove Inf
rawin(:,4) = max(rawin(:,4),0);
% get nonzero rows, i.e. the melody
melody = rawin(all(rawin,2),[1,4]);
% calculate the period of each sample
dt = rawin(2)-rawin(1);
% calculate the number of phrases, by counting the total number
% of "jumps" in time
temp = melody(2:length(melody),1)-melody(1:length(melody)-1,1);
temp2=ones(length(temp),1)*dt*1.5;
phrases = sum(temp-temp2>0);

t = melody(:,1);
pitch = melody(:,2);

pp0 = splinefit(t, pitch, phrases,"order",0);
y0 = floor(ppval(pp0,t));
fitness0 = sum(power(pitch-y0,2));

pp1 = splinefit(t, pitch, phrases,"order",1);
y1 = floor(ppval(pp1,t));
fitness1 = sum(power(pitch-y1,2));

pp2 = splinefit(t, pitch, phrases,"order",1);
y2 = floor(ppval(pp2,t));
fitness2 = sum(power(pitch-y2,2));

pp3 = splinefit(t, pitch, phrases,"order",3);
y3 = floor(ppval(pp3,t));
fitness3 = sum(power(pitch-y3,2));

pp4 = splinefit(t, pitch, phrases,"order",4);
y4 = floor(ppval(pp4,t));
fitness4 = sum(power(pitch-y4,2));

pp5 = splinefit(t, pitch, phrases,"order",5);
y5 = floor(ppval(pp5,t));
fitness5 = sum(power(pitch-y5,2));

% tp=1:length(t)*0.1;
% plot( t(tp),pitch(tp), ".", t(tp), y4(tp));

fit = [fitness0 fitness1 fitness2 fitness3 fitness4 fitness5 phrases length(melody)]

endfunction