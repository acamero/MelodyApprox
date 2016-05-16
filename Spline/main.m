
directory = "/home/andu/Documentos/uma/tfm/results/v2.0/melodia/";

Files=dir(strcat(directory,'*.csv'));
%frames
fit =1:6;

for k=1:length(Files)
   fileName=Files(k).name;
   temp = fitnessByPhrases(strcat(directory,fileName) );
   %temp = fitnessByFrames(strcat(directory,fileName) );
   fit = [fit;temp];
end

csvwrite("fitness-phrases.csv",fit);

