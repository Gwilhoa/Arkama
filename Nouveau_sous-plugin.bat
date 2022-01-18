@echo off
set /p name="Quel est le nom du sous plugin ? : "
mvn archetype:generate -DgroupId=fr.guigui205.arkama -DartifactId=%name%
PAUSE