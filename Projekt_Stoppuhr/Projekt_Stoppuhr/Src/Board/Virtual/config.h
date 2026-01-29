//*******************************************************************
/*!
\file   config.h
\author Thomas Breuer
\date   26.09.2022
\brief  Board specific configuration
*/

//*******************************************************************
/*
Board:    Virtual
*/

//*******************************************************************
using namespace EmbSysLib::Hw;
using namespace EmbSysLib::Dev;
using namespace EmbSysLib::Ctrl;
using namespace EmbSysLib::Mod;

//-------------------------------------------------------------------
// Port
//-------------------------------------------------------------------
Port_Virtual port( "localhost:1000" );

//-------------------------------------------------------------------
// Timer
//-------------------------------------------------------------------
Timer_Mcu   timer( 10000L/*us*/ );

TaskManager taskManager( timer );

//-------------------------------------------------------------------
// Display
//-------------------------------------------------------------------
//*******************************************************************
#include "../../Resource/Color/Color.h"

Font        fontFont_10x20      ( Memory_Mcu( "../../../Src/Resource/Font/font_10x20.bin"       ).getPtr() );
Font        fontFont_16x24      ( Memory_Mcu( "../../../Src/Resource/Font/font_16x24.bin"       ).getPtr() );
Font        fontFont_8x12       ( Memory_Mcu( "../../../Src/Resource/Font/font_8x12.bin"        ).getPtr() );
Font        fontFont_8x8        ( Memory_Mcu( "../../../Src/Resource/Font/font_8x8.bin"         ).getPtr() );

DisplayGraphic_Virtual  dispGraphic( 800, 480, "localhost:1000", fontFont_8x12, 2 );
ScreenGraphic lcd( dispGraphic );

//-------------------------------------------------------------------
// UART
//-------------------------------------------------------------------
Uart_Stdio uart( true );

Terminal   terminal( uart, 255,255,"# +" );

//-------------------------------------------------------------------
// Touch
//-------------------------------------------------------------------
Touch_Virtual  touch( "localhost:1000", 320, 240 );

Pointer        pointer( touch );

//-------------------------------------------------------------------
// Digital
//-------------------------------------------------------------------
Digital       LD1 ( port,16, Digital::Out , 0 );
Digital       LD2 ( port,17, Digital::Out , 0 );
Digital       Btn1( port, 5, Digital::In  , 0 );
Digital       Btn2( port, 6, Digital::In  , 0 );
Digital       Btn3( port, 7, Digital::In  , 0 );
Digital       User( port, 1, Digital::In  , 0 );
