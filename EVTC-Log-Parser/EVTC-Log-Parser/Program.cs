using EVTC_Log_Parser.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EVTC_Log_Parser
{
    public class Program
    {
        private static Parser _parser = new Parser();

        private static void PromptBegin()
        {
            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
            Console.Clear();
        }

        private static void PromptQuit()
        {
            Console.WriteLine("Press any key to quit...");
            Console.ReadKey();
            Environment.Exit(0);
        }

        public static void Main(string[] args)
        {
            PromptBegin();
            PromptQuit();
        }
    }
}
