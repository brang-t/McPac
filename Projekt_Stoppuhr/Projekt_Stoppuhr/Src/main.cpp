//*******************************************************************
#include <stdio.h>

#include "EmbSysLib.h"
#include "ReportHandler.h"
#include "config.h"

//*******************************************************************
class App : public Timer::Task
{
public:
  App( Timer *timer )
  {
    T = timer->getCycleTime()*1E-6f;
    timer->add( this );
  }

  void update() override
  {
    tickCnt++;
    if(running) runCnt++;
  }

  float getTime() const     { return (float)runCnt  * T; }
  float getWallTime() const { return (float)tickCnt * T; }

  void start() { running = true; }
  void pause() { running = false; }
  void reset() { runCnt = 0; }

  bool isRunning() const { return running; }

private:
  DWORD runCnt  = 0;
  DWORD tickCnt = 0;
  float T       = 0.0f;
  bool  running = false;
};

//*******************************************************************
static void drawStopwatchGraphics(ScreenGraphic &lcd, float time_s, bool forceRedraw)
{
  // ---------------- Display geometry ----------------
  const int LCD_W = lcd.getWidth();
  const int LCD_H = lcd.getHeight();

  const int section = LCD_W / 3;
  const int cy      = (int)(LCD_H * 0.45f);
  const int R_max   = 120;

  const int cx_ms  = (int)(section * 0.5f);
  const int cx_s   = (int)(section * 1.5f);
  const int cx_min = (int)(section * 2.5f);

  // ---------------- Time split ----------------
  int total_ms = (int)(time_s * 1000.0f);
  int ms  = total_ms % 1000;
  int sec = (total_ms / 1000) % 60;
  int min = (total_ms / 60000) % 5;

  if(total_ms >= 5 * 60 * 1000)
  {
    total_ms = ms = sec = min = 0;
  }

  // ---------------- Colors ----------------
  const WORD BLACK     = 0x0000;
  const WORD WHITE     = 0xFFFF;
  const WORD MS_COLOR  = (WORD)Color::Magenta;
  const WORD SEC_COLOR = (WORD)0x07E0; // green
  const WORD MIN_COLOR = (WORD)0xFBE0; // orange

  // ---------------- ms Tank geometry ----------------
  const int tankH  = 2 * R_max;
  const int tankW  = 48;
  const int frame  = 3;
  const int innerW = tankW - 2 * frame;
  const int innerH = tankH - 2 * frame;

  const int tankX = cx_ms - tankW / 2;
  const int tankY = cy - R_max;

  // Draw tank frame only once
  static bool tankFrameDrawn = false;
  if(!tankFrameDrawn)
  {
    lcd.drawRectangle((WORD)tankX, (WORD)tankY, (WORD)tankW, (WORD)tankH, WHITE);
    tankFrameDrawn = true;
  }

  // ---------------- ms Fill height logic ----------------
  const int REFRESH_MS = 200;

  // --- ms wrap + frame hold logic ---
  static int last_ms = -1;
  static int holdEmpty = 0;
  static int holdFull  = 0;

  bool wrapped = (last_ms >= 0 && ms < last_ms);
  last_ms = ms;

  // Bei Wrap: 2 Frames leer halten
  if(wrapped)
  {
      holdEmpty = 1;
  }

  // Kurz vor Sekundenende: 2 Frames voll halten
  if(ms >= (1000 - REFRESH_MS))
  {
      holdFull = 1;
  }

  int fillH = 0;

  if(holdEmpty > 0)
  {
      fillH = 0;
      holdEmpty--;
  }
  else if(holdFull > 0)
  {
      fillH = innerH;
      holdFull--;
  }
  else
  {
      // normaler proportionaler Verlauf
      fillH = (innerH * ms + 999) / 1000;
      if(fillH > innerH) fillH = innerH;
  }


  // Update tank inside only (frame bleibt)
  lcd.drawRectangle((WORD)(tankX + frame), (WORD)(tankY + frame),
                    (WORD)innerW, (WORD)innerH, BLACK);

  if(fillH > 0)
  {
    WORD fillX = (WORD)(tankX + frame);
    WORD fillY = (WORD)((tankY + frame + innerH) - fillH);
    lcd.drawRectangle(fillX, fillY, (WORD)innerW, (WORD)fillH, MS_COLOR);
  }

  // ---------------- Sekunden/Minuten-Rahmen (Ring) ----------------
  const int ring = 3;                 // Ringdicke
  const int R_in = R_max - ring;      // Innenradius (max. "Füllradius")

  static bool secFrameDrawn = false;
  static bool minFrameDrawn = false;

  if(!secFrameDrawn)
  {
    // weißer Ring: außen weiß, innen schwarz
    lcd.drawCircle((WORD)cx_s, (WORD)cy, (WORD)R_max, WHITE);
    lcd.drawCircle((WORD)cx_s, (WORD)cy, (WORD)R_in,  BLACK);
    secFrameDrawn = true;
  }

  if(!minFrameDrawn)
  {
    lcd.drawCircle((WORD)cx_min, (WORD)cy, (WORD)R_max, WHITE);
    lcd.drawCircle((WORD)cx_min, (WORD)cy, (WORD)R_in,  BLACK);
    minFrameDrawn = true;
  }

  // ---------------- Caches ----------------
  static int last_sec = -1;
  static int last_min = -1;

  static int last_ms10_t = -1;
  static int last_sec_t  = -1;
  static int last_min_t  = -1;

  // Force redraw after reset (nur innen leeren + Texte)
  if(forceRedraw)
  {
    last_sec = -1;
    last_min = -1;
    last_ms10_t = -1;
    last_sec_t  = -1;
    last_min_t  = -1;
    last_ms     = -1;

    // Innenflächen löschen, Ring bleibt
    lcd.drawCircle((WORD)cx_s,   (WORD)cy, (WORD)R_in, BLACK);
    lcd.drawCircle((WORD)cx_min, (WORD)cy, (WORD)R_in, BLACK);

    const int textY = cy + R_max + 10;
    lcd.drawRectangle(0, (WORD)(textY - 2), (WORD)LCD_W, (WORD)24, BLACK);
  }

  // ---------------- Sekunde: nur bei Änderung innen neu zeichnen ----------------
  if(sec != last_sec)
  {
    last_sec = sec;

    // innen löschen (Ring bleibt)
    lcd.drawCircle((WORD)cx_s, (WORD)cy, (WORD)R_in, BLACK);

    int r_s = (R_in * sec) / 60;  // max bei 60s = R_in
    if(r_s > 0)
      lcd.drawCircle((WORD)cx_s, (WORD)cy, (WORD)r_s, SEC_COLOR);
  }

  // ---------------- Minute: nur bei Änderung innen neu zeichnen ----------------
  if(min != last_min)
  {
    last_min = min;

    lcd.drawCircle((WORD)cx_min, (WORD)cy, (WORD)R_in, BLACK);

    int r_min = (R_in * min) / 5; // max bei 5min = R_in
    if(r_min > 0)
      lcd.drawCircle((WORD)cx_min, (WORD)cy, (WORD)r_min, MIN_COLOR);
  }

  // ---------------- Text: nur bei Änderung ----------------
  int ms10 = ms / 10;

  if(ms10 != last_ms10_t || sec != last_sec_t || min != last_min_t)
  {
    last_ms10_t = ms10;
    last_sec_t  = sec;
    last_min_t  = min;

    const int textY = cy + R_max + 10;
    lcd.drawRectangle(0, (WORD)(textY - 2), (WORD)LCD_W, (WORD)24, BLACK);

    lcd.setTextColor(WHITE);
    lcd.drawText(cx_ms  - 40, textY, "%03d ms",  ms);
    lcd.drawText(cx_s   - 30, textY, "%02d s",   sec);
    lcd.drawText(cx_min - 35, textY, "%01d min", min);
  }
}

//*******************************************************************
int main(void)
{
  lcd.clear();
  lcd.printf( 0, 0, __DATE__ "," __TIME__ );
  lcd.printf( 1, 0, "Grafische Stoppuhr" );
  lcd.refresh();

  App app( &timer );

  bool lastBtn1 = false;
  bool lastBtn2 = false;
  bool lastBtn3 = false;

  float lastRefreshWall = 0.0f;
  bool forceRedraw = true;

  while(1)
  {
    bool b1 = Btn1.get();
    bool b2 = Btn2.get();
    bool b3 = Btn3.get();

    if(b1 && !lastBtn1) { app.start(); }
    if(b2 && !lastBtn2) { app.pause(); }
    if(b3 && !lastBtn3) { app.reset(); forceRedraw = true; }

    lastBtn1 = b1;
    lastBtn2 = b2;
    lastBtn3 = b3;

    float wall = app.getWallTime();

    if((wall - lastRefreshWall) >= 0.2f)
    {
      lastRefreshWall += 0.2f;

      float t = app.getTime();
      lcd.printf( 2, 0, "Time: %6.3f sec", t );

      drawStopwatchGraphics(lcd, t, forceRedraw);
      forceRedraw = false;

      lcd.refresh();
    }

    System::delayMilliSec(1);
  }
}
