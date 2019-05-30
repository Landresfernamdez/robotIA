
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
        switch (c) {
          case '1':
            control.Motor(200,100);//ADELANTE
            delay(250);
            control.Motor(0,1);//APAGAR
            velocidad=150;//Reseteamos la velocidad
            break;
          case '2':
              control.Motor(-200,1);//izq adelante,der atras->izquierda
              delay(250);
              control.Motor(0,1);
              velocidad=150;//Reseteamos la velocidad
            break;
          case '3':
            control.Motor(200,-100);//ATRAS
            delay(250);
            control.Motor(0,1);//APAGAR
            velocidad=150;//Reseteamos la velocidad
            break;
          case '4':
              control.Motor(200,1);//izq atras,der adelante->derecha
              delay(250);
              control.Motor(0,1);
              velocidad=150;//Reseteamos la velocidad
            break;
          default:
            // if nothing else matches, do the default
            // default is optional
            break;
                    }
      }  
      
  }
