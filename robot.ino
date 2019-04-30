
#include <LEANTEC_ControlMotor.h>//Incluimos la librería control de motores 
// Configuramos los pines que vamos a usar
ControlMotor control(2,4,7,8,9,10); // MotorDer1,MotorDer2,MotorIzq1,MotorIzq2,PWM_Derecho,PWM_Izquierdo
int velocidad=150;//Declaramos una variable para guardar la velocidad. 150 es la velocidad en la que empezará.
const int LED = 13;
const int BTPWR = 11;
char nombreBT[10] ="TechKrowd";
char velocity ='4';//9600
char pin [5]= "0000";
void setup() 
{ //En esta ocasión no necesitamos configurar pines ya que la librería lo hace.
  
  pinMode(LED, OUTPUT);
  pinMode(BTPWR, OUTPUT);

  digitalWrite(LED, LOW);
  digitalWrite(BTPWR, HIGH);

  Serial.begin(9600);

  Serial.print("AT");
  delay(1000);

  Serial.print("AT+NAME");
  Serial.print(nombreBT);
  delay(1000);

  Serial.print("AT+BAUD");
  Serial.print(velocity);
  delay(1000);

  Serial.print("AT+PIN");
  Serial.print(pin);
  delay(1000);

  digitalWrite(LED, HIGH);
  delay(1000);
   digitalWrite(LED, LOW);
} 

void loop() 
    {
      if(Serial.available())  
      {  
       char c = Serial.read();  
       if(c=='1')
       {
         digitalWrite(LED, HIGH);
         //Los dos motores giran en sentido horario.
        //Si estuvieran montados en un robot este giraria a la derecha. 
          control.Motor(200,-100);//ADELANTE
          delay(3000);
          control.Motor(0,1);//APAGAR
          velocidad=150;//Reseteamos la velocidad
       }
       if(c=='2')
       {
         digitalWrite(LED, LOW);
         //Un motor gira en sentido horario y el otro antihorario
          //Si estuvierán montados en un robot el robot retrocedería. 
            control.Motor(-180,1);//izq atras,der adelante->derecha
            delay(3000);
            control.Motor(0,1);
            velocidad=150;//Reseteamos la velocidad
       }
       if(c=='3')
       {
         digitalWrite(LED, HIGH);
         //Los dos motores giran en sentido horario.
        //Si estuvieran montados en un robot este giraria a la derecha. 
          control.Motor(200,100);//ATRAS
          delay(3000);
          control.Motor(0,1);//APAGAR
          velocidad=150;//Reseteamos la velocidad
       }
       if(c=='4')
       {
         digitalWrite(LED, LOW);
         //Un motor gira en sentido horario y el otro antihorario
          //Si estuvierán montados en un robot el robot retrocedería. 
            control.Motor(180,1);//izq adelante,der atras->izquierda
            delay(3000);
            control.Motor(0,1);
            velocidad=150;//Reseteamos la velocidad
       }
      }  
      
  }
