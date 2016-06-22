function fit = fitnessByPhrasesFixed(filePath)

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
temp2 = ones(length(temp),1)*dt*1.5;
temp3 = temp - temp2;

fitness0 = 0;
fitness1 = 0;
fitness2 = 0;
fitness3 = 0;
fitness4 = 0;
fitness5 = 0;
phrases = 0;

j = 1;
for i=1:length(temp3),

   if temp3(i)>0,
    t = melody(j:i,1);
    pitch = melody(j:i,2);
    
    pp0 = polyfit(t, pitch, 0);
    y0 = floor(polyval(pp0,t));
    fitness0 += sum(power(pitch-y0,2));

    pp1 = polyfit(t, pitch, 1);
    y1 = floor(polyval(pp1,t));
    fitness1 += sum(power(pitch-y1,2));

    pp2 = polyfit(t, pitch, 1);
    y2 = floor(polyval(pp2,t));
    fitness2 += sum(power(pitch-y2,2));

    pp3 = polyfit(t, pitch, 3);
    y3 = floor(polyval(pp3,t));
    fitness3 += sum(power(pitch-y3,2));

    pp4 = polyfit(t, pitch, 4);
    y4 = floor(polyval(pp4,t));
    fitness4 += sum(power(pitch-y4,2));
    
    pp5 = polyfit(t, pitch, 5);
    y5 = floor(polyval(pp5,t));
    fitness5 += sum(power(pitch-y5,2));
    
    j = i+1;   
    phrases++;
   endif

endfor


fit = [fitness0 fitness1 fitness2 fitness3 fitness4 fitness5 phrases length(melody)]

endfunction