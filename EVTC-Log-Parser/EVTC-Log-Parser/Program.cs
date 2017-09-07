using EVTC_Log_Parser.Model;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;

namespace EVTC_Log_Parser
{
    public class Program
    {
        #region Members
        private static string[] _logs;
        private static Parser _parser = new Parser();
        #endregion

        #region Main
        [STAThread]
        public static void Main(string[] args)
        {
            PromptBegin();
            if (LoadEVTC())
            {
                List<string> err = new List<string>();
                for (int i = 0; i < _logs.Length; i++)
                {
                    Console.Write(String.Format(Properties.Resources.ParsingProgressFormat, i + 1, _logs.Length));
                    if (_parser.Parse(_logs[i]))
                    {
                        Console.ForegroundColor = ConsoleColor.Green;
                        Console.WriteLine(Properties.Resources.Success);
                        Console.ResetColor();
                        Converter c = new Converter(_parser);
                    }
                    else
                    {
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.WriteLine(Properties.Resources.Fail);
                        Console.ResetColor();
                        err.Add(_logs[i]);
                    }
                }
            }
            Console.WriteLine();
            PromptQuit();
        }
        #endregion

        #region Private Methods
        private static void PromptBegin()
        {
            Console.Write(Properties.Resources.PressAnyKeyToContinue);
            Console.ReadKey();
            Console.Clear();
        }

        private static void PromptQuit()
        {
            Console.Write(Properties.Resources.PressAnyKeyToQuit);
            Console.ReadKey();
            Environment.Exit(0);
        }

        private static bool LoadEVTC()
        {
            _logs = Directory.EnumerateFiles("./", "*", SearchOption.AllDirectories).Where(f => f.EndsWith(Properties.Resources.Evtc) || f.EndsWith(Properties.Resources.EvtcZip)).ToArray();
            if (_logs.Length > 0)
            {
                return true;
            }
            else
            {
                Console.Write(Properties.Resources.EmptyDirectoryWarning);
                return false;
            }
        }
        #endregion

    }
}
